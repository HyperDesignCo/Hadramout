package com.hyperdesign.myapplication.presentation.menu.mvi

import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse

data class MenuStateModel(
    val menuData : MenueResponse? = null,
    val MealDetailsData : MealDetailsResponseEntity? = null,
    val AddToCartData : AddToCartResponseEntity? = null,
    val showCartDate : CartResponseEntity? = null,
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val branchId : Int = 0,
    val mealId : Int = 0,
    val quantity : String = "",
    val sizeId : String = "",
    val choices : Map<String,List<String>> = emptyMap()
)
