package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

// Data Layer: DTOs
data class MenuResponseDto(
    val menus: List<MenuDto>,
    val message: String
)

data class MenuDto(
    val id: String,
    val title: String,
    val image: String,
    val meals: List<MealDto>
)

data class MealDto(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val price: String,
    @SerializedName("discount_price")
    val discountPrice: String
)

