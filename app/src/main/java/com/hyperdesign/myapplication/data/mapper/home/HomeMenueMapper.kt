package com.hyperdesign.myapplication.data.mapper.home

import com.hyperdesign.myapplication.data.dto.AdsDto
import com.hyperdesign.myapplication.data.dto.HomeMenuDTO
import com.hyperdesign.myapplication.data.dto.HomeResponseDTO
import com.hyperdesign.myapplication.data.dto.MealMenuDto
import com.hyperdesign.myapplication.data.dto.SlideShow
import com.hyperdesign.myapplication.domain.Entity.AdsEntity
import com.hyperdesign.myapplication.domain.Entity.HomeMenu
import com.hyperdesign.myapplication.domain.Entity.HomeResponse
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.domain.Entity.SlideShowEntity

fun MealMenuDto.toDomain(): Meal {
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
fun AdsDto.toDomain(): AdsEntity{
    return AdsEntity(
        id = id,
        image = image,
        mealId = mealId,
        orderId = orderId
    )
}

fun SlideShow.toDomain(): SlideShowEntity{
    return SlideShowEntity(
        id=id,
        image = image,
        title = title,
        text = text,
        mealId = mealId,
        orderId = orderId
    )
}

fun HomeResponseDTO.toDomain(): HomeResponse {
    return HomeResponse(
        phone = phone,
        homeMenus = homeMenus.map { it.toDomain() },
        bestSalesMeals = bestSalesMeals.map { it.toDomain() },
        ads = ads?.map { it.toDomain() },
        slideshow =slideshow?.map { it.toDomain() },
        message = message
    )
}