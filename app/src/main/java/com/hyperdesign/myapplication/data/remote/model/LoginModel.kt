package com.hyperdesign.myapplication.data.remote.model

import com.google.gson.annotations.SerializedName



data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    val user : User,
    val message : String,


)

data class User(
    val id: Int,
    val name : String,
    val email : String,
    val image : String,
    val mobile : String
)
