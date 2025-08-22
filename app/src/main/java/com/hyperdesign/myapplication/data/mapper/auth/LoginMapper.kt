package com.hyperdesign.myapplication.data.mapper.auth

import com.hyperdesign.myapplication.data.remote.dto.LoginResponse
import com.hyperdesign.myapplication.data.remote.dto.User
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.UserEntity


fun LoginResponse.toLoginDomain(): LoginEntity {
    return LoginEntity(
        accessToken = accessToken,
        tokenType = tokenType,
        user = user.toDomain(),
        message = message
    )
}

fun User.toDomain(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        image = image,
        mobile = mobile
    )
}