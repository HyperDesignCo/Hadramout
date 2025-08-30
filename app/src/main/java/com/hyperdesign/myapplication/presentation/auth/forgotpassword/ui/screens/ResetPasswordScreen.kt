package com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.CreatePasswordIntents
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.CreatePasswordModelState
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi.CreatePasswordViewModel
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.widgets.ForgetPasswordHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import org.koin.androidx.compose.koinViewModel


@Composable
fun ResetPasswordScreen(email:String?,viewModel: CreatePasswordViewModel= koinViewModel()){
    val navController = LocalNavController.current
    val state by viewModel.createPasswordState.collectAsStateWithLifecycle()
    val email =email
    viewModel.email.value = email.toString()
    val context = LocalContext.current

    Log.d("loading",state.isLoading.toString())
    LaunchedEffect(Unit) {

        viewModel.createPasswordChannel.collect {
            when(it){
                is ValidationEvent.Success ->{
                    Toast.makeText(context,
                        context.getString(R.string.password_updated_successfully), Toast.LENGTH_SHORT).show()

                    navController.popBackStack()


                }
                is ValidationEvent.Failure -> {
                    Toast.makeText(context,it.errorMessage, Toast.LENGTH_SHORT).show()

                }
            }

        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ResetPasswordScreenContent(
            onBackPressed = {
                navController.popBackStack()

            },
            state = state,
            onResetPassword = {
                viewModel.handleIntent(
                    CreatePasswordIntents.changePasswordClick(
                        state.newPassword
                    )
                )
            },
            onChangePassword = { viewModel.handleIntent(CreatePasswordIntents.onChangeNewPassword(it)) }


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
fun ResetPasswordScreenContent(onResetPassword:()->Unit,onChangePassword:(String)->Unit,state: CreatePasswordModelState, onBackPressed: () -> Unit ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ForgetPasswordHeader(
            title = stringResource(R.string.reset_password),
            body = stringResource(R.string.enter_your_new_password),
            onBackPressesd = onBackPressed
        )


            Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                stringResource(id = R.string.enter_new_password),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = state.newPassword,
                onValueChange = {
                    onChangePassword(it)
                },
                isError = state.newPasswordError!=null,
                errorMessage = state.newPasswordError,
                isPassword = true,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_new_password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(70.dp))

            CustomButton(
                text = stringResource(R.string.reset_password),
                onClick = {
                    onResetPassword()
                },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203)
            )

        }
    }

        }