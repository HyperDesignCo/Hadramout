package com.hyperdesign.myapplication.data.mapper.auth

import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.User
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.UserEntity


fun LoginResponse?.toLoginDomain(): LoginEntity {
    if (this == null) return LoginEntity(
        accessToken = "", tokenType = "",
        user = null.toDomain(), message = ""
    )
    return LoginEntity(
        accessToken = accessToken.orEmpty(),
        tokenType = tokenType.orEmpty(),
        user = user.toDomain(),
        message = message.orEmpty()
    )
}

fun User?.toDomain(): UserEntity {
    if (this == null) return UserEntity(
        id = 0,
        name = "",
        mobile = "",
        image = "",
        balance = "",
        email = "",
        authenticated = ""
    )
    return UserEntity(
        id = id ?: 0,
        name = name.orEmpty(),
        image = image.orEmpty(),
        mobile = mobile.orEmpty(),
        authenticated = authenticated.orEmpty(),
        email = email.orEmpty(),
        balance = balance.orEmpty()
    )
}