package com.hyperdesign.myapplication.data.mapper.auth

import com.hyperdesign.myapplication.data.dto.ForgetPasswordResponseDto
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordModeEntity


fun ForgetPasswordResponseDto.toDomain(): ForgetPasswordModeEntity {
    return ForgetPasswordModeEntity(
        message = message,
        code = code
    )
}