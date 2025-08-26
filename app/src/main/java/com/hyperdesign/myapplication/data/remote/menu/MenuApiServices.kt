package com.hyperdesign.myapplication.data.remote.menu

import com.hyperdesign.myapplication.data.dto.MealDetailResponseDto
import com.hyperdesign.myapplication.data.dto.MenuResponseDto

interface MenuApiServices {
    suspend fun getMenus(branchId: Int): MenuResponseDto

    suspend fun showMealDetails(branchId: Int, mealId: Int): MealDetailResponseDto
}