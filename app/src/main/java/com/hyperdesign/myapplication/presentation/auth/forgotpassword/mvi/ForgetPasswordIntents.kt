package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi



sealed class ForgetPasswordIntents {

    data class OnEmailChanged(val email: String) : ForgetPasswordIntents()

    data class PasswordForgetClickAction(val email: String) : ForgetPasswordIntents()
}