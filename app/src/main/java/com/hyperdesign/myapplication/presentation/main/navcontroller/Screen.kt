package com.hyperdesign.myapplication.presentation.main.navcontroller

import android.net.Uri
import com.google.gson.Gson
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.Featured


// Sealed class for all screens
sealed class Screen(val route: String, val title: Int? = null, val icon: Int? = null) {
    object LoginInScreen : Screen(ScreensConst.LOGIN_IN_SCREEN)
    object LoginStepTwoScreen : Screen(ScreensConst.LOGIN_SCREEN_STEP_TWO)
    object ForgotPasswordScreen : Screen(ScreensConst.FORGOT_PASSWORD_SCREEN)
    object VerifyScreen : Screen(ScreensConst.Verify_Screen)

    object SignUpScreen : Screen(ScreensConst.Sign_Up_Screen)

    object MealDetailsScreen : Screen("${ScreensConst.MEAL_DETAILS_SCREEN}/{mealJson}")
    object ResetPasswordScreen : Screen(ScreensConst.RESET_PASSWORD_SCREEN)
    object HomeScreen : Screen(ScreensConst.HOME_SCREEN, R.string.home, R.drawable.home)
    object ProfileScreen : Screen(ScreensConst.PROFILE_SCREEN, R.string.profile, R.drawable.profile)
    object MenueScreen : Screen(ScreensConst.MENUE_SCREEN, R.string.menu, R.drawable.menu)
}




fun goToScreenMealDetails(meal: Featured): String {
    val json = Uri.encode(Gson().toJson(meal))
    return "${ScreensConst.MEAL_DETAILS_SCREEN}/$json"
}
