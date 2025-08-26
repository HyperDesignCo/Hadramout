package com.hyperdesign.myapplication.domain.Entity


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