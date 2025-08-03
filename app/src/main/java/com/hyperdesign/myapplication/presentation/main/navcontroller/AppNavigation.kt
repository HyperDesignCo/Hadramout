package com.hyperdesign.myapplication.presentation.main.navcontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ForgotPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ResetPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.VerifyScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreenStepTwo


val LocalNavController = compositionLocalOf<NavController> {
    error("No NavController provided")
}
@Composable
fun AppNavigation(destenation:String) {

    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = destenation) {
            composable(Screen.LoginInScreen.route) {
                LoginScreen()
            }
            composable(Screen.LoginStepTwoScreen.route) {
                LoginScreenStepTwo()

            }
            composable(Screen.ForgotPasswordScreen.route) {
                ForgotPasswordScreen()
            }

            composable(Screen.VerifyScreen.route) {
                VerifyScreen()
            }

            composable(Screen.ResetPasswordScreen.route) {
                ResetPasswordScreen()
            }

        }
    }

}