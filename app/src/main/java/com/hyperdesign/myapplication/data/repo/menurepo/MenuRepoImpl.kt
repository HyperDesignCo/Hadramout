package com.hyperdesign.myapplication.data.repo.menurepo

import com.hyperdesign.myapplication.data.mapper.menu.toDomain
import com.hyperdesign.myapplication.data.remote.menu.MenuApiServices
import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class MenuRepoImpl(
    private val menuApiServices: MenuApiServices
): MenuRepo {
    override suspend fun getMenus(branchId: Int): MenueResponse {
        val response = menuApiServices.getMenus(branchId)
        return response.toDomain()
    }

    override suspend fun showMealDetails(
        branchId: Int,
        mealId: Int
    ): MealDetailsResponseEntity {
        val response = menuApiServices.showMealDetails(branchId, mealId)
        return response.toDomain()
    }

    override suspend fun addMealToCart(addToCartRequest: AddOrderRequest): AddToCartResponseEntity {
        val response = menuApiServices.addMealToCart(addToCartRequest)
        return response.toDomain()
    }
}