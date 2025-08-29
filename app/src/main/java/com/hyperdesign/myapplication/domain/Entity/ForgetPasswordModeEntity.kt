package com.hyperdesign.myapplication.domain.Entity


data class ForgetPasswordModeEntity(
    val message: String,
    val code: String
)

data class ForgetPasswordRequest(
    val email: String
)