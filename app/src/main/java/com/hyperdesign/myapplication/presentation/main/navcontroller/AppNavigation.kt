package com.hyperdesign.myapplication.presentation.main.navcontroller

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ForgotPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ResetPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.VerifyScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreenStepTwo
import com.hyperdesign.myapplication.presentation.home.ui.screens.HomeScreen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.ui.screens.MenuScreen
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
                composable(Screen.ForgotPasswordScreen.route) { ForgotPasswordScreen() }
                composable(Screen.VerifyScreen.route) { VerifyScreen() }
                composable(Screen.ResetPasswordScreen.route) { ResetPasswordScreen() }
                composable(Screen.HomeScreen.route) { HomeScreen() }
                composable(Screen.ProfileScreen.route) { ProfileScreen() }
                composable(Screen.MenueScreen.route) { MenuScreen() }
            }
        }
    }
}

