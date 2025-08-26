package com.hyperdesign.myapplication.domain.Entity

data class MenueResponse(
    val menus: List<MenuEntity>,
    val message: String

)
data class MenuEntity(
    val id: String,
    val title: String,
    val imageUrl: String,
    val meals: List<Meal>
)

data class MealEntity(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val discountPrice: Double?
)