package com.hyperdesign.myapplication.data.mapper.auth

import com.hyperdesign.myapplication.data.dto.RegisterResponse
import com.hyperdesign.myapplication.domain.Entity.RegisterModelEntity


fun RegisterResponse.toRegisterDomain(): RegisterModelEntity {
    return RegisterModelEntity(
        accessToken = accessToken,
        tokenType = tokenType,
        user = user.toDomain(),
        message = message
    )
}