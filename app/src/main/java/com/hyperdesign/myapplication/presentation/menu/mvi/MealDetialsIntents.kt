package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class MealDetialsIntents{
    data class showMealDetails(val branchId: Int,val mealId: Int): MealDetialsIntents()
    data class changeBranchId(val branchId: Int) : MealDetialsIntents()
    data class changeMealId(val mealId: Int) : MealDetialsIntents()

}
