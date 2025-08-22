package com.hyperdesign.myapplication.presentation.auth.signup.mvi

sealed class RegisterIntents {

    data class NameChanged(val name: String): RegisterIntents()

    data class EmailChanged(val email: String): RegisterIntents()

    data class MobileChanged(val mobile: String): RegisterIntents()

    data class PasswordChanged(val password: String): RegisterIntents()

    data class ConfirmPasswordChanged(val confirmPassword: String): RegisterIntents()

    data class RegisterClicked(val name: String, val email: String, val mobile: String, val password: String): RegisterIntents()
}