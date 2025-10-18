package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName

data class LoginEntity(
    val accessToken: String,
    val tokenType: String,
    val user: UserEntity,
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String,
    @SerializedName("device_token")
    val deviceToken: String? = null,
)


data class UserEntity(
    val id: Int,
    val name: String,
    val email: String,
    val image: String,
    val mobile: String,
    val authenticated:String
)
