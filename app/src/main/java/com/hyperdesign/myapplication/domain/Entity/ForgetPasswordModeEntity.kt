package com.hyperdesign.myapplication.domain.Entity


data class ForgetPasswordModeEntity(
    val message: String,
    var code: String
)

data class ForgetPasswordRequest(
    val email: String
)