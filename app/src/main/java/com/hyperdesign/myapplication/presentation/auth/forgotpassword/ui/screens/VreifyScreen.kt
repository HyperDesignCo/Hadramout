package com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.gson.Gson
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordModeEntity
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.ForgetPasswordModelState
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.VerifyIntent
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.VerifyModelState
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.VerifyViewModel
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import org.koin.androidx.compose.koinViewModel


@Composable
fun VerifyScreen(verifyJson:String?,email:String?,viewModel: VerifyViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val code = verifyJson?.let { Gson().fromJson(it, ForgetPasswordModeEntity::class.java) }
    viewModel.code.value = code?.code.toString()

    Log.d("code", viewModel.code.value)

    Log.d("email",email.toString())

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ValidationEvent.Success -> {
                    val encodedEmail = Uri.encode(email ?: "")
                    navController.navigate(Screen.ResetPasswordScreen.route.replace("{email}", encodedEmail)) {

                        popUpTo(Screen.VerifyScreen.route) { inclusive = true }
                    }
                }
                is ValidationEvent.Failure -> {
                    Toast.makeText(context,effect.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        VerifyScreenContent(
            state = state,
            onIntent = viewModel::processIntent,
            email=email,
            onBackPressed = {
                viewModel.processIntent(VerifyIntent.BackPressed)
                navController.popBackStack()
            }
        )
        if (state.isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Secondry)
            }
        }
    }

}

@Composable
fun VerifyScreenContent(
    state: VerifyModelState,
    onIntent: (VerifyIntent) -> Unit,
    onBackPressed: () -> Unit,
    email: String?
) {
    val focusRequesters = remember { List(5) { FocusRequester() } }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        MainHeader(
            showBackPressedIcon = true,
            showLogo = true,
            onBackPressesd = onBackPressed,
            cardCount = 0
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                stringResource(id = R.string.verify),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(id = R.string.your_identity),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                stringResource(id = R.string.Enter_your_code),
                color = Secondry,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Text(
                email?:"",
                color = Secondry,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                repeat(5) { index ->
                    Box(modifier = Modifier.size(40.dp)) {
                        CustomTextField(
                            value = state.codeDigits[index],
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    onIntent(VerifyIntent.UpdateDigit(index, newValue))
                                    if (newValue.isNotEmpty() && index < 4) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            textColor = Color.Black,
                            borderWidth = 2f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequesters[index])
                                .onKeyEvent { event ->
                                    if (event.key == Key.Backspace && state.codeDigits[index].isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                        true
                                    } else {
                                        false
                                    }
                                },
                            isPassword = false,
                            keyboardType = KeyboardType.Number
                        )
                    }
                    if (index < 4) {
                        Spacer(modifier = Modifier.width(5.dp))
                    }
                }
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = state.error ?: "",
                    color = Color.Red,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            CustomButton(
                text = stringResource(R.string.coninue),
                onClick = {
                    onIntent(VerifyIntent.SubmitCode)
                },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203),

            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            Button(
                onClick = {
                    onIntent(VerifyIntent.ResendCode)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(26.dp)
                    ),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.LightGray
                )
            ) {
                Text(
                    text = stringResource(R.string.dont_receive_code),
                    fontSize = 16.sp
                )
            }
        }
    }
}