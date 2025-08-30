package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

data class VerifyModelState(
    val codeDigits: List<String> = List(5) { "" },
    val isLoading: Boolean = false,
    val error: String? = null,
    val isVerified: Boolean = false
)
