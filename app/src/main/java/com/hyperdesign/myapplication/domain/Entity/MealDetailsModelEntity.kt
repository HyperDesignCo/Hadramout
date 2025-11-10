package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName


data class MealDetailsResponseEntity(
    val meal: MealDetailsEntity,
    val message: String

)
data class MealDetailsEntity(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val sizes: List<SizeEntity>,
    val price: Double,
    val discountPrice: Double?,
    val choices: List<ChoiceEntity>
)

data class SizeEntity(
    val id: String,
    val sizeId: String,
    val sizeTitle: String,
    val price: Double,
    val discountPrice: Double?
)

data class ChoiceEntity(
    val id: String,
    val min: Int,
    val max: Int,
    val choiceTitle: String,
    val subChoices: List<SubChoiceEntity>
)

data class SubChoiceEntity(
    val id: String,
    val title: String,
    val price: Double
)

data class AddToCartResponseEntity(
    val message: String
)

data class AddOrderRequest(
    @SerializedName("branch_id") val branchId: String,
    @SerializedName("meal_id") val mealId: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("area_id") val areaId :String,
    @SerializedName("size_id") val sizeId: String,
    @SerializedName("choices") val choices: Map<String, List<String>>,
    @SerializedName("device_id") val deviceId :String,
    @SerializedName("pickup_status") val pickupStatus :String
)