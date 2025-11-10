package com.hyperdesign.myapplication.domain.Entity



data class RegisterModelEntity(
    val accessToken: String,
    val tokenType: String,
    val user: UserEntity,
    val message: String
)

data class RegisterRequst(
    val email: String,
    val password: String,
    val name: String,
    val mobile: String,
    val device_token: String,
    val device_type: String,
    val device_id: String
)