package com.hyperdesign.myapplication.presentation.auth.signup.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.auth.signup.mvi.RegisterIntents
import com.hyperdesign.myapplication.presentation.auth.signup.mvi.RegisterViewModel
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(registerViewModel: RegisterViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val state by registerViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state) {
        registerViewModel.validationEvent.collectLatest { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.SignUpScreen.route) { inclusive = true }
                    }
                }
                is ValidationEvent.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

        SignUpScreenContent(
            name = state.userName,
            email = state.email,
            phone = state.phoneNumber,
            password = state.password,
            confirmPassword = state.confirmPassword,
            onNameChange = { registerViewModel.onIntentEvent(RegisterIntents.NameChanged(it)) },
            onEmailChange = { registerViewModel.onIntentEvent(RegisterIntents.EmailChanged(it)) },
            onPhoneChange = { registerViewModel.onIntentEvent(RegisterIntents.MobileChanged(it)) },
            onPasswordChange = { registerViewModel.onIntentEvent(RegisterIntents.PasswordChanged(it)) },
            onConfirmPasswordChange = { registerViewModel.onIntentEvent(RegisterIntents.ConfirmPasswordChanged(it)) },
            onSignUpClick = { registerViewModel.onIntentEvent(RegisterIntents.RegisterClicked(name = state.userName, email = state.email, mobile = state.phoneNumber, password = state.password)) },
            nameError = state.userNameError,
            emailError = state.emailError,
            isLoading = state.isLoading,
            phoneError = state.phoneNumberError,
            passwordError = state.passwordError,
            confirmPasswordError = state.confirmPasswordError,
        )



}

@Composable
fun SignUpScreenContent(
    name: String = "",
    email: String = "",
    phone: String = "",
    password: String = "",
    isLoading: Boolean = false,
    confirmPassword: String = "",
    onNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPhoneChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onSignUpClick: () -> Unit = {},
    nameError: String? = null,
    emailError: String? = null,
    phoneError: String? = null,
    passwordError: String? = null,
    confirmPasswordError: String? = null,
) {
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
            .verticalScroll(verticalScroll)
    ) {
        HadramoutHeader(title = stringResource(R.string.create_new_account))

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.user_name),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = name,
                onValueChange = onNameChange,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_your_name),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
                isError = nameError != null,
                errorMessage = nameError
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = R.string.email_address),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = email,
                onValueChange = onEmailChange,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_email_address),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = R.string.phone_number),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = phone,
                onValueChange = onPhoneChange,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_phone_number),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Phone,
                isError = phoneError != null,
                errorMessage = phoneError
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = R.string.password),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = password,
                onValueChange = onPasswordChange,
                textColor = Color.Black,
                borderWidth = 2f,
                isPassword = true,
                placeholder = stringResource(R.string.enter_your_password),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
                isError = passwordError != null,
                errorMessage = passwordError
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = R.string.confirm_password),
                color = Secondry,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            CustomTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                textColor = Color.Black,
                borderWidth = 2f,
                isPassword = true,
                placeholder = stringResource(R.string.enter_your_confirm_password),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = stringResource(R.string.create_new_account),
                onClick = {
                    onSignUpClick()
                },
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.already_have_an_account),
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = stringResource(R.string.login),
                    color = Color(0xFFF15A25),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold

                )
            }




        }
    }
    if (isLoading) {
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