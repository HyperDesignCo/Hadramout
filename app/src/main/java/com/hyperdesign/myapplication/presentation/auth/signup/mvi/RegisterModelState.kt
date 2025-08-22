package com.hyperdesign.myapplication.presentation.auth.signup.mvi

import com.hyperdesign.myapplication.domain.Entity.RegisterModelEntity

data class RegisterModelState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val password: String = "",
    val registerRespnse : RegisterModelEntity? = null,
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val errorMessage: String? = null


)