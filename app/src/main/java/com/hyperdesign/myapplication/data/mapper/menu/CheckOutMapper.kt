package com.hyperdesign.myapplication.data.mapper.menu

import com.hyperdesign.myapplication.data.dto.CheckOutResponseDto
import com.hyperdesign.myapplication.data.dto.PaymentMethodDto
import com.hyperdesign.myapplication.data.mapper.home.toDomain
import com.hyperdesign.myapplication.domain.Entity.CheckOutResponseEntity
import com.hyperdesign.myapplication.domain.Entity.PaymentMethodEntity

fun CheckOutResponseDto.toDomain(): CheckOutResponseEntity{
    return CheckOutResponseEntity(
        cart = cart.toEntity(),
        message = message,
        addresses = addresses.map { it.toDomain() },
        payment_methods = payment_methods.map { it.toDomain() }
    )
}

fun PaymentMethodDto.toDomain(): PaymentMethodEntity{
    return PaymentMethodEntity(
        id = id,
        title = title,
        image = image,
        show_apple = show_apple,
        show_android = show_android
    )
}


