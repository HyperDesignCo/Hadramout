package com.hyperdesign.myapplication.presentation.main.navcontroller

import MenuScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ForgotPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ResetPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.VerifyScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreenStepTwo
import com.hyperdesign.myapplication.presentation.auth.signup.ui.SignUpScreen
import com.hyperdesign.myapplication.presentation.home.ui.screens.HomeScreen
import com.hyperdesign.myapplication.presentation.menu.ui.MealDetailsScreen
import com.hyperdesign.myapplication.presentation.menu.ui.screens.CartScreen
import com.hyperdesign.myapplication.presentation.menu.ui.screens.CheckOutScreen
import com.hyperdesign.myapplication.presentation.profile.ui.screens.ProfileScreen

val bottomNavScreens = listOf(
    Screen.HomeScreen,
    Screen.MenueScreen,
    Screen.ProfileScreen
)

val LocalNavController = compositionLocalOf<NavController> {
    error("No NavController provided")
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry)
        .value?.destination?.route

    // Determine if the current screen is part of the bottom navigation
    val isBottomNavScreen = bottomNavScreens.any { it.route == currentRoute }

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            bottomBar = {
                if (isBottomNavScreen) {
                    BottomNavigationBar(navController = navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.LoginInScreen.route) { LoginScreen() }
                composable(Screen.LoginStepTwoScreen.route) { LoginScreenStepTwo() }
                composable(Screen.ForgotPasswordScreen.route)
                {
                    ForgotPasswordScreen()
                }
                composable(Screen.VerifyScreen.route, arguments = listOf(navArgument("verifyJson"){type=
                    NavType.StringType},navArgument("email"){type=NavType.StringType}))
                {backStackEntry ->
                    val verifyJson = backStackEntry.arguments?.getString("verifyJson")
                    val email = backStackEntry.arguments?.getString("email")
                    VerifyScreen(verifyJson = verifyJson, email = email)
                }
                composable(Screen.ResetPasswordScreen.route, arguments =
                    listOf(navArgument("email"){
                        type= NavType.StringType
                    })
                ){backStack->
                    val email = backStack.arguments?.getString("email")
                    ResetPasswordScreen(email = email)
                }
                composable(Screen.HomeScreen.route) { HomeScreen() }
                composable(Screen.ProfileScreen.route) { ProfileScreen() }
                composable(Screen.MenueScreen.route) { MenuScreen() }
                composable(
                    Screen.MealDetailsScreen.route,
                    arguments = listOf(navArgument("mealJson") { type = NavType.StringType }),

                ) {backStackEntry ->
                    val mealJson = backStackEntry.arguments?.getString("mealJson")
                    MealDetailsScreen(mealJson = mealJson)


                }
                composable(Screen.CheckOutScreen.route) { CheckOutScreen() }
                composable(Screen.CartScreen.route) { CartScreen() }
                composable(Screen.SignUpScreen.route) { SignUpScreen() }
            }
        }
    }
}

