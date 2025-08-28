package com.hyperdesign.myapplication.data.mapper.menu

import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.ChoiceDto
import com.hyperdesign.myapplication.data.dto.MealDetailResponseDto
import com.hyperdesign.myapplication.data.dto.MealDetailsDto
import com.hyperdesign.myapplication.data.dto.SizeDto
import com.hyperdesign.myapplication.data.dto.SubChoiceDto
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.ChoiceEntity
import com.hyperdesign.myapplication.domain.Entity.MealDetailsEntity
import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.SizeEntity
import com.hyperdesign.myapplication.domain.Entity.SubChoiceEntity


fun MealDetailResponseDto.toDomain(): MealDetailsResponseEntity {
    return MealDetailsResponseEntity(
        meal = meal.toDomain(),
        message = message
    )


}
fun MealDetailsDto.toDomain(): MealDetailsEntity {
    return MealDetailsEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = image,
        price = price.toDoubleOrNull()?:0.0,
        discountPrice = discountPrice.toDoubleOrNull()?:0.0,
        sizes = sizes.map { it.toDomain() },
        choices = choices.map { it.toDomain() },

    )
}

fun SizeDto.toDomain(): SizeEntity {
    return SizeEntity(
        id = id,
        sizeId = sizeId,
        sizeTitle = sizeTitle,
        price = price.toDoubleOrNull()?:0.0,
        discountPrice = discountPrice.toDoubleOrNull()?:0.0,

    )
}

fun ChoiceDto.toDomain(): ChoiceEntity {
    return ChoiceEntity(
        id = id,
        min = min.toIntOrNull()?:0,
        max = max.toIntOrNull()?:0,
        choiceTitle = choiceTitle,
        subChoices = subChoices.map { it.toDomain() },

    )
}

fun SubChoiceDto.toDomain(): SubChoiceEntity {
    return SubChoiceEntity(
        id = id,
        title = title,
        price = price.toDoubleOrNull() ?: 0.0,
    )
}

fun AddToCartResponseDto.toDomain(): AddToCartResponseEntity {
    return AddToCartResponseEntity(
        message = message
    )
}