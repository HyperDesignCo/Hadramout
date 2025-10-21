package com.hyperdesign.myapplication.data.mapper.menu

import com.hyperdesign.myapplication.data.dto.BranchInfoDTO
import com.hyperdesign.myapplication.data.dto.CheckOutResponseDto
import com.hyperdesign.myapplication.data.dto.PaymentMethodDto
import com.hyperdesign.myapplication.data.mapper.home.toDomain
import com.hyperdesign.myapplication.domain.Entity.BranchInfoEntity
import com.hyperdesign.myapplication.domain.Entity.CheckOutResponseEntity
import com.hyperdesign.myapplication.domain.Entity.PaymentMethodEntity

fun CheckOutResponseDto.toDomain(): CheckOutResponseEntity{
    return CheckOutResponseEntity(
        branch = branch?.toDomain(),
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


fun BranchInfoDTO.toDomain(): BranchInfoEntity{
    return BranchInfoEntity(
        id = id,
        titleAr = titleAr,
        titleEn = titleEn,
        slug = slug,
        addressAr = addressAr,
        addressEn = addressEn,
        deliveryCost = deliveryCost,
        lat = lat,
        lng = lng,
        status = status,
        busyHours = busyHours,
        busyEndAt = busyEndAt,
        openTime = openTime,
        closeTime = closeTime,
        pickupStatus = pickupStatus,
        pickupBusyHours = pickupBusyHours,
        createAt = createAt,
        updateAt = updateAt,
        deleteAt = deleteAt

    )
}

