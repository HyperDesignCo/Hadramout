package com.hyperdesign.myapplication.presentation.main.navcontroller

import MenuScreen
import android.util.Log
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
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens.AllAddressesScreen
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens.UpdateAddressScreen
import com.hyperdesign.myapplication.presentation.profile.common.ui.screens.ProfileScreen
import com.hyperdesign.myapplication.presentation.profile.myorders.ui.screens.MyOrderScreen
import com.hyperdesign.myapplication.presentation.profile.settings.contactus.ui.screens.ContactUsScreen
import com.hyperdesign.myapplication.presentation.profile.settings.common.ui.screens.SettingsScreen
import com.hyperdesign.myapplication.presentation.profile.settings.privacypolicy.ui.screens.PrivacyPolicyScreen
import com.hyperdesign.myapplication.presentation.profile.settings.returnpolicy.ui.screens.ReturnPolicyScreen
import com.hyperdesign.myapplication.presentation.profile.settings.termsandconditions.ui.screens.TermsAndConditionsScreen
import com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.ui.screens.WhoAreWeScreen

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

    Log.i("NavControllerScreen", "Current Route: $currentRoute")

    // Determine if the current screen is part of the bottom navigation
    val isBottomNavScreen = bottomNavScreens.any { it.route == currentRoute }
//            || currentRoute.toString() in Screen.MyOrders.route

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
                composable(Screen.MyOrders.route) { MyOrderScreen() }
                composable(Screen.SettingsScreen.route) { SettingsScreen() }
                composable(Screen.WhoAreWeScreen.route) { WhoAreWeScreen() }
                composable(Screen.ContactUsScreen.route) { ContactUsScreen() }
                composable(Screen.ReturnPolicyScreen.route) { ReturnPolicyScreen() }
                composable(Screen.TermesAndConditionsScreen.route) { TermsAndConditionsScreen() }
                composable(Screen.PrivacyPolicyScreen.route) { PrivacyPolicyScreen() }
                composable(Screen.AllAddressesScreen.route, arguments = listOf(navArgument("screenType"){
                    type =  NavType.StringType
                })) {navBackStack->
                    val screenType = navBackStack.arguments?.getString("screenType")
                    AllAddressesScreen(screenType)
                }
                composable(Screen.UpdateAddressScreen.route, arguments = listOf(navArgument("addressId"){type=
                    NavType.StringType},navArgument("lat"){NavType.StringType},navArgument("long"){NavType.StringType}))
                {navBackStack->
                    val addressId = navBackStack.arguments?.getString("addressId")
                    val lat =  navBackStack.arguments?.getString("lat")
                    val long = navBackStack.arguments?.getString("long")

                    UpdateAddressScreen(addressId = addressId.orEmpty(), lat = lat.orEmpty(), long = long.orEmpty())




                }

            }
        }
    }
}


