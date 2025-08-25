package com.hyperdesign.myapplication.data.mapper.home

import com.hyperdesign.myapplication.data.dto.HomeMenuDTO
import com.hyperdesign.myapplication.data.dto.HomeResponseDTO
import com.hyperdesign.myapplication.data.dto.MealDTO
import com.hyperdesign.myapplication.domain.Entity.HomeMenu
import com.hyperdesign.myapplication.domain.Entity.HomeResponse
import com.hyperdesign.myapplication.domain.Entity.Meal

fun MealDTO.toDomain(): Meal {
    return Meal(
        id = id.toIntOrNull() ?: 0,
        title = title,
        description = description,
        image = image,
        price = price.toDoubleOrNull() ?: 0.0,
        discountPrice = discountPrice.toDoubleOrNull()
    )
}

fun HomeMenuDTO.toDomain(): HomeMenu {
    return HomeMenu(
        id = id.toIntOrNull() ?: 0,
        title = title,
        meals = meals.map { it.toDomain() }
    )
}

fun HomeResponseDTO.toDomain(): HomeResponse {
    return HomeResponse(
        homeMenus = homeMenus.map { it.toDomain() },
        bestSalesMeals = bestSalesMeals.map { it.toDomain() },
        message = message
    )
}