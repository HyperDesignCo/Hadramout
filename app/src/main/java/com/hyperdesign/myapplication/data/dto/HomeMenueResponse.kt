package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName


data class HomeResponseDTO(
    @SerializedName("home_menus")
    val homeMenus: List<HomeMenuDTO>,
    @SerializedName("best_sales_meals")
    val bestSalesMeals: List<MealDTO>,
    val message: String
)

data class HomeMenuDTO(
    val id: String,
    val title: String,
    val meals: List<MealDTO>
)

data class MealDTO(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val price: String,
    @SerializedName("discount_price")
    val discountPrice: String
)







