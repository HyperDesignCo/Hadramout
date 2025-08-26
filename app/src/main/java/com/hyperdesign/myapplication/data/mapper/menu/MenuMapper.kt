package com.hyperdesign.myapplication.data.mapper.menu

import com.hyperdesign.myapplication.data.dto.MealDto
import com.hyperdesign.myapplication.data.dto.MenuDto
import com.hyperdesign.myapplication.data.dto.MenuResponseDto
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.domain.Entity.MenuEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse


fun MenuResponseDto.toDomain(): MenueResponse{
    return MenueResponse(
        menus = menus.map { it.toDomain() },
        message = message

    )
}

fun MenuDto.toDomain(): MenuEntity {
    return MenuEntity(
        id = id,
        title = title,
        imageUrl = image,
        meals = meals.map { it.toDomain() }
    )
}

fun MealDto.toDomain(): Meal {
    return Meal(
        id = id.toIntOrNull() ?: 0,
        title = title,
        description = description,
        image = image,
        price = price.toDoubleOrNull() ?: 0.0,
        discountPrice = discountPrice.toDoubleOrNull() ?: 0.0
    )
}