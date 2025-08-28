package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class MealDetailResponseDto(
    val meal: MealDetailsDto,
    val message: String
)

data class MealDetailsDto(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val sizes: List<SizeDto>,
    val price: String,
    @SerializedName("discount_price")
    val discountPrice: String,
    val choices: List<ChoiceDto>
)

data class SizeDto(
    val id: String,
    @SerializedName("size_id")
    val sizeId: String,
    @SerializedName("size_title")
    val sizeTitle: String,
    val price: String,
    @SerializedName("discount_price")
    val discountPrice: String
)

data class ChoiceDto(
    val id: String,
    val min: String,
    val max: String,
    @SerializedName("choice_title")
    val choiceTitle: String,
    @SerializedName("sub_choices")
    val subChoices: List<SubChoiceDto>
)

data class SubChoiceDto(
    val id: String,
    val title: String,
    val price: String
)


data class AddToCartResponseDto(
    val message: String
)

