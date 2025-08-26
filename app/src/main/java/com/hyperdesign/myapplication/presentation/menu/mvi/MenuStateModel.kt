package com.hyperdesign.myapplication.presentation.menu.mvi

import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse

data class MenuStateModel(
    val menuData : MenueResponse? = null,
    val MealDetailsData : MealDetailsResponseEntity? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val branchId : Int = 0,
    val mealId : Int = 0
)
