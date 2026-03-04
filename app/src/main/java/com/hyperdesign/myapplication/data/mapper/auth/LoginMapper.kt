package com.hyperdesign.myapplication.data.mapper.auth

import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.User
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.UserEntity


fun LoginResponse.toLoginDomain(): LoginEntity {
    return LoginEntity(
        accessToken = accessToken.orEmpty(),
        tokenType = tokenType.orEmpty(),
        user = user?.toDomain() ?: UserEntity(id = 0, name = "", mobile = "", image = "", balance = "", email = "",authenticated = ""),
        message = message.orEmpty()
    )
}

fun User.toDomain(): UserEntity {
    return UserEntity(
        id = id?:0,
        name = name.orEmpty(),
        image = image.orEmpty(),
        mobile = mobile.orEmpty(),
        authenticated=authenticated.orEmpty(),
        email = email.orEmpty(),
        balance=balance.orEmpty()
    )
}