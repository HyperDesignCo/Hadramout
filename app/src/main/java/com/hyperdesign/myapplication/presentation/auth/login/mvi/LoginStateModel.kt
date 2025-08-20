package com.hyperdesign.myapplication.presentation.auth.login.mvi

import com.hyperdesign.myapplication.data.remote.model.LoginResponse
import com.hyperdesign.myapplication.domain.Entity.LoginEntity

data class LoginStateModel(
    val phoneNumber: String = "",
    val password: String = "",
    val phoneNumberError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val loginResponse: LoginEntity? = null,
    val errorMessage: String? = null
)