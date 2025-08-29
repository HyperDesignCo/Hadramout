package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordModeEntity

data class ForgetPasswordModelState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val ForgetPsasswordResponse : ForgetPasswordModeEntity? = null
)
