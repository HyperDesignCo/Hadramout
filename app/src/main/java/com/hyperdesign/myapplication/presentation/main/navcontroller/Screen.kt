package com.hyperdesign.myapplication.presentation.main.navcontroller

import com.hyperdesign.myapplication.R


// Sealed class for all screens
sealed class Screen(val route: String, val title: Int? = null, val icon: Int? = null) {
    object LoginInScreen : Screen(ScreensConst.LOGIN_IN_SCREEN)
    object LoginStepTwoScreen : Screen(ScreensConst.LOGIN_SCREEN_STEP_TWO)
    object ForgotPasswordScreen : Screen(ScreensConst.FORGOT_PASSWORD_SCREEN)
    object VerifyScreen : Screen(ScreensConst.Verify_Screen)

    object SignUpScreen : Screen(ScreensConst.Sign_Up_Screen)
    object ResetPasswordScreen : Screen(ScreensConst.RESET_PASSWORD_SCREEN)
    object HomeScreen : Screen(ScreensConst.HOME_SCREEN, R.string.home, R.drawable.home)
    object ProfileScreen : Screen(ScreensConst.PROFILE_SCREEN, R.string.profile, R.drawable.profile)
    object MenueScreen : Screen(ScreensConst.MENUE_SCREEN, R.string.menu, R.drawable.menu)
}

