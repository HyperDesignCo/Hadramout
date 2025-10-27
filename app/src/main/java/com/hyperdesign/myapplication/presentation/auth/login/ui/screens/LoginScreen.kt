package com.hyperdesign.myapplication.presentation.auth.login.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginIntents
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginStateModel
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Location permission handling
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Trigger permission request on first composition
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.validationEvents.collectLatest { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.LoginInScreen.route) { inclusive = true }
                    }
                }
                is ValidationEvent.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //

    LoginScreenContent(
        phoneNumber = state.phoneNumber,
        password = state.password,
        phoneNumberError = state.phoneNumberError,
        passwordError = state.passwordError,
        isLoading = state.isLoading,
        onPhoneNumberChange = { viewModel.onIntent(LoginIntents.PhoneNumberChanged(it)) },
        onPasswordChange = { viewModel.onIntent(LoginIntents.PasswordChanged(it)) },
        onLoginClick = { viewModel.onIntent(LoginIntents.LoginEvent(state.phoneNumber, state.password)) },
        onGoToForgetPasswordScreen = {
            navController.navigate(Screen.ForgotPasswordScreen.route)
        },
        onCreateAccountClick = {
            navController.navigate(Screen.SignUpScreen.route)
        },
        onGuestLoginClick = {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.LoginInScreen.route) { inclusive = true }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    phoneNumber: String,
    password: String,
    phoneNumberError: String?,
    passwordError: String?,
    isLoading: Boolean,
    onPhoneNumberChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoToForgetPasswordScreen: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onGuestLoginClick: () -> Unit
) {
    val verticalScroll = rememberScrollState()
    val phoneFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(verticalScroll)
    ) {
        HadramoutHeader()

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.email_or_phoneNumber),
                color = Secondry,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(7.dp))
            CustomTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_your_email_or_phone),
                modifier = Modifier.fillMaxWidth(),
                borderColor = Color(0xFFFCB203),
                errorBorderColor = Color.Red,
                keyboardType = KeyboardType.Text,
                isError = phoneNumberError != null,
                errorMessage = phoneNumberError,
                focusRequester = phoneFocusRequester,
                nextFocusRequester = passwordFocusRequester
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.password),
                color = Secondry,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(7.dp))

            CustomTextField(
                value = password,
                onValueChange = onPasswordChange,
                textColor = Color.Black,
                borderWidth = 2f,
                placeholder = stringResource(R.string.enter_your_password),
                modifier = Modifier.fillMaxWidth(),
                borderColor = Color(0xFFFCB203),
                errorBorderColor = Color.Red,
                keyboardType = KeyboardType.Password,
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError,
                focusRequester = passwordFocusRequester
            )

            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = stringResource(R.string.forgot_password),
                color = Secondry,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGoToForgetPasswordScreen() },
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = stringResource(R.string.login),
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                )
                Text(
                    text = stringResource(R.string.dont_have_an_account),
                    color = Secondry,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CustomButton(
                text = stringResource(R.string.create_new_account),
                onClick = onCreateAccountClick,
                modifier = Modifier.fillMaxWidth(),
                startColor = Color(0xFFF15A25),
                endColor = Color(0xFFFCB203)
            )

            Spacer(modifier = Modifier.height(13.dp))

            Button(
                onClick = onGuestLoginClick,
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
                    text = stringResource(R.string.login_as_a_guest),
                    fontSize = 16.sp
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