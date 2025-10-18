package com.hyperdesign.myapplication.data.mapper.profile

import com.hyperdesign.myapplication.data.dto.EditProfileResponse
import com.hyperdesign.myapplication.data.dto.User
import com.hyperdesign.myapplication.data.dto.UserDTO
import com.hyperdesign.myapplication.domain.Entity.EditProfileResponseEntity
import com.hyperdesign.myapplication.domain.Entity.UserEntity

fun EditProfileResponse.toEntity(): EditProfileResponseEntity {
    return EditProfileResponseEntity(
        message = message,
        user = user.toEntity()
    )
}

fun User.toEntity(): UserEntity{
    return UserEntity(
        id = id,
        name = name,
        email = email,
        image = image,
        mobile =mobile,
        authenticated=authenticated
    )
}
