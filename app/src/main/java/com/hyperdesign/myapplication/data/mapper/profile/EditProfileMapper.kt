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
        id = id?:0,
        name = name.orEmpty(),
        image = image.orEmpty(),
        mobile =mobile.orEmpty(),
        authenticated=authenticated.orEmpty(),
        balance=balance.orEmpty(),
        email = email.orEmpty()
    )
}
