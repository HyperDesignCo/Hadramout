package com.hyperdesign.myapplication.presentation.auth.login.mvi

sealed class LoginIntents {

    data class LoginEvent(val phone: String, val pass: String) : LoginIntents()
    data class PhoneNumberChanged(val phone: String) : LoginIntents()
    data class PasswordChanged(val password: String) : LoginIntents()
}