package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName


data class NewPasswordRequest(
    val email : String,
    @SerializedName("new_password")
    val newPassword : String ,
    @SerializedName("device_token")
    val deviceToken:String,

)