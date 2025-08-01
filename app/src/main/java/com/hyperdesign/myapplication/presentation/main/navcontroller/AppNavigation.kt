package com.hyperdesign.myapplication.presentation.main.navcontroller

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreenStepTwo

@Composable
fun AppNavigation(destenation:String) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = destenation) {
        composable(Screen.LoginInScreen.route){
            LoginScreen(
                onClickToScreenLoginTwo = {
                    navController.navigate(Screen.LoginStepTwoScreen.route)
                }
            )
        }
        composable(Screen.LoginStepTwoScreen.route) {
            LoginScreenStepTwo()

        }

    }

}