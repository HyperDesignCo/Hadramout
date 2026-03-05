package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("token_type")
    val tokenType: String? = null,
    val user: User? = null,
    val message: String? = null,
)

