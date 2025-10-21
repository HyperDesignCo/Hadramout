package com.hyperdesign.myapplication.data.mapper.menu

import com.hyperdesign.myapplication.data.dto.CartDto
import com.hyperdesign.myapplication.data.dto.CartMealDto
import com.hyperdesign.myapplication.data.dto.CartResponseDto
import com.hyperdesign.myapplication.domain.Entity.CartEntity
import com.hyperdesign.myapplication.domain.Entity.CartMealEntity
import com.hyperdesign.myapplication.domain.Entity.CartResponseEntity

fun CartResponseDto.toEntity(): CartResponseEntity {
    return CartResponseEntity(
        cart = cart?.toEntity(),
        message = message.orEmpty()
    )
}

fun CartDto.toEntity(): CartEntity {
    return CartEntity(
        id = id.orEmpty(),
        serviceCharge = serviceCharge.toDoubleOrNull() ?: 0.0,
        deliveryCost = deliveryCost.toDoubleOrNull() ?: 0.0,
        vat = vat.toDoubleOrNull() ?: 0.0,
        primaryPrice = primaryPrice.toDoubleOrNull() ?: 0.0,
        vatCost = vatCost.toDoubleOrNull() ?: 0.0,
        serviceChargeCost = serviceChargeCost.toDoubleOrNull() ?: 0.0,
        coupon = coupon.orEmpty(),
        couponDiscount = couponDiscount.toDoubleOrNull() ?: 0.0,
        couponCost = couponCost.toDoubleOrNull() ?: 0.0,
        offerDiscount = offerDiscount.toDoubleOrNull() ?: 0.0,
        netPrice = netPrice?.toDoubleOrNull() ?: 0.0,
        totalPrice = totalPrice.toDoubleOrNull() ?: 0.0,
        specialRequests = specialRequests.orEmpty(),
        freeDelivery = freeDelivery == "1",
        cartMeals = cartMeals?.map { it.toEntity() } ?: emptyList(),
        pickUpStatus = pickupStatus
    )
}

fun CartMealDto.toEntity(): CartMealEntity {
    return CartMealEntity(
        id = id.orEmpty(),
        mealId = mealId.orEmpty(),
        mealTitle = mealTitle.orEmpty(),
        mealImage = mealImage.orEmpty(),
        price = price.toDoubleOrNull() ?: 0.0,
        quantity = quantity.toIntOrNull() ?: 0,
        comment = comment.orEmpty(),
        sizeId = sizeId.orEmpty(),
        sizeTitle = sizeTitle.orEmpty(),
        cartItemId = cartItemId.orEmpty(),
        subChoicesPrice = subChoicesPrice.toDoubleOrNull() ?: 0.0,
        netPrice = netPrice?.toDoubleOrNull() ?: 0.0,
        primaryPrice = primaryPrice.toDoubleOrNull() ?: 0.0,
        totalPrice = totalPrice.toDoubleOrNull() ?: 0.0,
        choices = choices ?: emptyList()
    )
}