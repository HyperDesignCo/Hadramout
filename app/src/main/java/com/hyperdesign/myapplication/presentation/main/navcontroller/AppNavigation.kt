package com.hyperdesign.myapplication.presentation.main.navcontroller

import MenuScreen
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ForgotPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.ResetPasswordScreen
import com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.screens.VerifyScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreen
import com.hyperdesign.myapplication.presentation.auth.login.ui.screens.LoginScreenStepTwo
import com.hyperdesign.myapplication.presentation.auth.signup.ui.SignUpScreen
import com.hyperdesign.myapplication.presentation.common.wedgits.MapScreen
import com.hyperdesign.myapplication.presentation.home.ui.screens.HomeScreen
import com.hyperdesign.myapplication.presentation.menu.ui.MealDetailsScreen
import com.hyperdesign.myapplication.presentation.menu.ui.MealInput
import com.hyperdesign.myapplication.presentation.menu.ui.screens.CartScreen
import com.hyperdesign.myapplication.presentation.menu.ui.screens.CheckOutScreen
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens.AllAddressesScreen
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens.UpdateAddressScreen
import com.hyperdesign.myapplication.presentation.profile.common.ui.screens.ProfileScreen
import com.hyperdesign.myapplication.presentation.profile.common.ui.screens.UserProfileScreen
import com.hyperdesign.myapplication.presentation.profile.myorders.ui.screens.MyOrderScreen
import com.hyperdesign.myapplication.presentation.profile.settings.contactus.ui.screens.ContactUsScreen
import com.hyperdesign.myapplication.presentation.profile.settings.common.ui.screens.SettingsScreen
import com.hyperdesign.myapplication.presentation.profile.settings.privacypolicy.ui.screens.PrivacyPolicyScreen
import com.hyperdesign.myapplication.presentation.profile.settings.returnpolicy.ui.screens.ReturnPolicyScreen
import com.hyperdesign.myapplication.presentation.profile.settings.termsandconditions.ui.screens.TermsAndConditionsScreen
import com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.ui.screens.WhoAreWeScreen
import java.net.URLDecoder

val bottomNavScreens = listOf(
    Screen.HomeScreen,
    Screen.MenueScreen,
    Screen.ProfileScreen
)

val LocalNavController = compositionLocalOf<NavController> {
    error("No NavController provided")
}

fun routeWithoutQuery(route: String?): String {
    return route?.substringBefore("?") ?: ""
}

@SuppressLint("RememberReturnType")
@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()

    // Current destination (with possible query string)
    val currentBackStackEntry = navController.currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry)
        .value

    val currentRoute = currentBackStackEntry?.destination?.route
    Log.i("NavControllerScreen", "Current Route (raw): $currentRoute")

    // Clean route – without query part
    val cleanCurrentRoute = routeWithoutQuery(currentRoute)

    // Show bottom bar only on the three bottom‑nav screens
    val isBottomNavScreen = bottomNavScreens.any { it.route == cleanCurrentRoute }

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
                /* ------------------- HOME SCREEN ------------------- */
                composable(
                    route = "${Screen.HomeScreen.route}?lat={lat}&lng={lng}&pickup={pickup}",
                    arguments = listOf(
                        navArgument("lat") { type = NavType.StringType; defaultValue = "" },
                        navArgument("lng") { type = NavType.StringType; defaultValue = "" },
                        navArgument("pickup") { type = NavType.StringType; defaultValue = "" }
                    )
                ) { backStackEntry ->
                    Log.d(
                        "AppNavigation",
                        "HomeScreen args → lat=${backStackEntry.arguments?.getString("lat")} " +
                                "lng=${backStackEntry.arguments?.getString("lng")} " +
                                "pickup=${backStackEntry.arguments?.getString("pickup")}"
                    )
                    HomeScreen(navBackStackEntry = backStackEntry)
                }

                /* ------------------- OTHER SCREENS ------------------- */
                composable(Screen.LoginInScreen.route) { LoginScreen() }
                composable(Screen.LoginStepTwoScreen.route) { LoginScreenStepTwo() }
                composable(Screen.ForgotPasswordScreen.route) { ForgotPasswordScreen() }

                composable(
                    Screen.VerifyScreen.route,
                    arguments = listOf(
                        navArgument("verifyJson") { type = NavType.StringType },
                        navArgument("email") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val verifyJson = backStackEntry.arguments?.getString("verifyJson")
                    val email = backStackEntry.arguments?.getString("email")
                    VerifyScreen(verifyJson = verifyJson, email = email)
                }

                composable(
                    Screen.ResetPasswordScreen.route,
                    arguments = listOf(navArgument("email") { type = NavType.StringType })
                ) { backStack ->
                    val email = backStack.arguments?.getString("email")
                    ResetPasswordScreen(email = email)
                }

                composable(Screen.ProfileScreen.route) { ProfileScreen() }
                composable(Screen.MenueScreen.route) { MenuScreen() }

                composable(
                    Screen.MealDetailsScreen.route,
                    arguments = listOf(navArgument("mealJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val mealJsonRaw = backStackEntry.arguments?.getString("mealJson")
                    val mealInput = mealJsonRaw?.let { raw ->
                        try {
                            val decoded = try { URLDecoder.decode(raw, "UTF-8") } catch (e: Exception) { raw }
                            val meal = Gson().fromJson(decoded, Meal::class.java)
                            if (meal != null) {
                                Log.d("NavGraph", "Parsed MealJson: $meal")
                                MealInput.MealJson(decoded)
                            } else {
                                MealInput.MealId(decoded.removeSurrounding("\"", "\"").trim())
                            }
                        } catch (e: Exception) {
                            Log.w("NavGraph", "Failed to parse as MealJson, treating as MealId: $raw")
                            MealInput.MealId(raw.removeSurrounding("\"", "\"").trim())
                        }
                    }
                    MealDetailsScreen(mealJson = mealInput)
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

                composable(
                    "${Screen.AllAddressesScreen.route}?lat={lat}&lng={lng}&areaId={areaId}",
                    arguments = listOf(
                        navArgument("lat") { type = NavType.StringType; defaultValue = "" },
                        navArgument("lng") { type = NavType.StringType; defaultValue = "" },
                        navArgument("areaId") { type = NavType.StringType; defaultValue = "" },
                        navArgument("screenType") { type = NavType.StringType })
                ) { navBackStack ->
                    val screenType = navBackStack.arguments?.getString("screenType")
                    AllAddressesScreen(screenType,navBackStack)
                }

                composable(
                    "${Screen.UpdateAddressScreen.route}?lat={lat}&lng={lng}&areaId={areaId}",
                    arguments = listOf(
                        navArgument("areaId") { type = NavType.StringType ;defaultValue = "" },
                        navArgument("lat") { type = NavType.StringType;defaultValue = ""  },
                        navArgument("long") { type = NavType.StringType;defaultValue = ""  },
                        navArgument("addressId") { type = NavType.StringType }
                    )
                ) { navBackStack ->
                    val addressId = navBackStack.arguments?.getString("addressId")
                    UpdateAddressScreen(
                        addressId = addressId.orEmpty(),
                        navBackStackEntry=navBackStack

                    )
                }

                composable(Screen.UserProfileScreen.route) { UserProfileScreen() }

                composable(
                    Screen.MapScreen.route,
                    arguments = listOf(
                        navArgument("navigateFrom") {
                            type = NavType.StringType
                            defaultValue = ""
                        },
                        navArgument("addressId") {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) { backStackEntry ->
                    val navigateFrom = backStackEntry.arguments?.getString("navigateFrom")?.trim() ?: ""
                    val addressId = backStackEntry.arguments?.getString("addressId")?.let { Uri.decode(it) } ?: ""  // Decode here
                    Log.d("AppNavigation", "MapScreen navigateFrom: $navigateFrom, addressId: $addressId")  // Log for debugging
                    MapScreen(navigateFrom = navigateFrom, addressId = addressId)  // Pass addressId directly
                }
            }
        }
    }
}