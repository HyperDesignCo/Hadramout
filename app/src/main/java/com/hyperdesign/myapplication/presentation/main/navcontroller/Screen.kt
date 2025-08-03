package com.hyperdesign.myapplication.presentation.main.navcontroller

sealed class Screen(val route: String) {

    object LoginInScreen : Screen(ScreensConst.LOGIN_IN_SCREEN)
    object LoginStepTwoScreen : Screen(ScreensConst.LOGIN_SCREEN_STEP_TWO)
    object ForgotPasswordScreen : Screen(ScreensConst.FORGOT_PASSWORD_SCREEN)

    object VerifyScreen : Screen(ScreensConst.Verify_Screen)

    object ResetPasswordScreen : Screen(ScreensConst.RESET_PASSWORD_SCREEN)

}