package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName



data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String?=null,
    @SerializedName("token_type")
    val tokenType: String?=null,
    val user : User?=null,
    val message : String?=null,


)

data class User(
    val id: Int?=null,
    val name : String?=null,
    val email : String?=null,
    val image : String?=null,
    val mobile : String?=null,
    val authenticated:String?=null,
    val balance:String?=null
)
