package com.hyperdesign.myapplication.domain.repo.menu

import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse

interface MenuRepo {
    suspend fun getMenus(branchId: Int): MenueResponse

    suspend fun showMealDetails(branchId: Int, mealId: Int): MealDetailsResponseEntity

    suspend fun addMealToCart(addToCartRequest: AddOrderRequest): AddToCartResponseEntity
}