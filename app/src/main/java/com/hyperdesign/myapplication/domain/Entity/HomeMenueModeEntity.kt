package com.hyperdesign.myapplication.domain.Entity

data class HomeResponse(
    val homeMenus: List<HomeMenu>,
    val bestSalesMeals: List<Meal>,
    val message: String
)

data class HomeMenu(
    val id: Int,
    val title: String,
    val meals: List<Meal>
)

data class Meal(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val price: Double,
    val discountPrice: Double?
)