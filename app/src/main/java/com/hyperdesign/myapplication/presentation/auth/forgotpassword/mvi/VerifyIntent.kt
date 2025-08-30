package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

sealed class VerifyIntent {
    data class UpdateDigit(val index: Int, val value: String) : VerifyIntent()
    object SubmitCode : VerifyIntent()
    object ResendCode : VerifyIntent()
    object BackPressed : VerifyIntent()
}