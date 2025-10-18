package com.hyperdesign.myapplication.domain.Entity

import com.hyperdesign.myapplication.data.dto.User


data class EditProfileResponseEntity(
    val message:String,
    val user: UserEntity
)

data class EditProfileRequest(
    val name: String,
    val email: String,
    val mobile: String,
    val image: String?
)