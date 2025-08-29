package com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.ForgetPasswordIntents
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.ForgetPasswordViewModel
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.widgets.ForgetPasswordHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForgotPasswordScreen(forgetPasswordViewModel: ForgetPasswordViewModel= koinViewModel()){
    val forgetPasswordState by forgetPasswordViewModel.forgetPasswordState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        forgetPasswordViewModel.forgetPasswordChannel.collectLatest { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigate(Screen.VerifyScreen.route){
                        popUpTo(Screen.ForgotPasswordScreen.route) { inclusive = true }

                    }
                }
                is ValidationEvent.Failure -> {
                    Toast.makeText(context,event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()

    ) {
        ForGotPasswordScreenContent(
            onBackPressed = {
            navController.popBackStack()
        }, onClickToVerifyScreen = {
            forgetPasswordViewModel.handleIntents(
                ForgetPasswordIntents.PasswordForgetClickAction(forgetPasswordState.email)
            )
        },
            password = forgetPasswordState.email,
            passwordError = forgetPasswordState.emailError,
            onPasswordChange = {
                forgetPasswordViewModel.handleIntents(
                    ForgetPasswordIntents.OnEmailChanged(
                        it
                    )
                )
            }
        )
        if (forgetPasswordState.isLoading) {
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
fun ForGotPasswordScreenContent(password: String,passwordError: String?=null,onPasswordChange: (String) -> Unit,onBackPressed: () -> Unit,onClickToVerifyScreen: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        ForgetPasswordHeader(onBackPressesd = onBackPressed)

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                stringResource(id = R.string.email_address),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = password,
                onValueChange = {
                    onPasswordChange(it)
                },
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder =stringResource(id = R.string.email_address),
                keyboardType = KeyboardType.Text,
                isError = passwordError!=null,
                errorMessage = passwordError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(70.dp))

            CustomButton(
                text = stringResource(R.string.send),
                onClick = {
                    onClickToVerifyScreen()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }


}