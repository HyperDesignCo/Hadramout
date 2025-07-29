package com.hyperdesign.myapplication.presentation.main.navcontroller

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(destenation:String) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = destenation) {
        composable(Screen.LoginInScreen.route){
            // LoginInScreen(navController = navController)
            Column {
                Text("This is the Login In Screen")
            }
        }

    }

}