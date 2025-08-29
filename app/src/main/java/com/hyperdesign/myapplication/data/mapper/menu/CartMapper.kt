package com.hyperdesign.myapplication.data.mapper.menu

import com.hyperdesign.myapplication.data.dto.CartDto
import com.hyperdesign.myapplication.data.dto.CartMealDto
import com.hyperdesign.myapplication.data.dto.CartResponseDto
import com.hyperdesign.myapplication.domain.Entity.CartEntity
import com.hyperdesign.myapplication.domain.Entity.CartMealEntity
import com.hyperdesign.myapplication.domain.Entity.CartResponseEntity

fun CartResponseDto.toEntity(): CartResponseEntity {
    return CartResponseEntity(
        cart = cart.toEntity(),
        message = message
    )
}

fun CartDto.toEntity(): CartEntity {
    return CartEntity(
        id = id,
        serviceCharge = serviceCharge?.toDoubleOrNull() ?: 0.0,
        deliveryCost = deliveryCost?.toDoubleOrNull() ?: 0.0,
        vat = vat?.toDoubleOrNull() ?: 0.0,
        primaryPrice = primaryPrice?.toDoubleOrNull() ?: 0.0,
        vatCost = vatCost?.toDoubleOrNull() ?: 0.0,
        serviceChargeCost = serviceChargeCost?.toDoubleOrNull() ?: 0.0,
        coupon = coupon ?: "",
        couponDiscount = couponDiscount?.toDoubleOrNull() ?: 0.0,
        couponCost = couponCost?.toDoubleOrNull() ?: 0.0,
        offerDiscount = offerDiscount?.toDoubleOrNull() ?: 0.0,
        netPrice = netPrice?.toDoubleOrNull() ?: 0.0,
        totalPrice = totalPrice?.toDoubleOrNull() ?: 0.0,
        specialRequests = specialRequests ?: "",
        freeDelivery = (freeDelivery?.toIntOrNull() ?: 0) == 1,
        cartMeals = cartMeals.map { it.toEntity() }
    )
}

fun CartMealDto.toEntity(): CartMealEntity {
    return CartMealEntity(
        id = id ?: "",
        mealId = mealId ?: "",
        mealTitle = mealTitle ?: "",
        mealImage = mealImage ?: "",
        price = (price ?: "").takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
        quantity = (quantity ?: "").takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0,
        comment = comment ?: "",
        sizeId = sizeId ?: "",
        sizeTitle = sizeTitle ?: "",
        cartItemId = cartItemId ?: "",
        subChoicesPrice = (subChoicesPrice ?: "").takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
        netPrice = (netPrice ?: "").takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
        primaryPrice = (primaryPrice ?: "").takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
        totalPrice = (totalPrice ?: "").takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
        choices = choices ?: emptyList()
    )
}