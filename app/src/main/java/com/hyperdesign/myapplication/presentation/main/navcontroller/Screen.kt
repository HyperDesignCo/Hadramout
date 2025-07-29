package com.hyperdesign.myapplication.presentation.main.navcontroller

sealed class Screen(val route: String) {

    object LoginInScreen : Screen(ScreensConst.LOGIN_IN_SCREEN)

}