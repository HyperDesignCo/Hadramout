package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class MealDetialsIntents{
    data class showMealDetails(val branchId: Int,val mealId: Int): MealDetialsIntents()
    data class changeBranchId(val branchId: Int) : MealDetialsIntents()
    data class changeMealId(val mealId: Int) : MealDetialsIntents()
    data class changeQuantity(val quantity: String) : MealDetialsIntents()
    data class changeSizeId(val sizeId: String) : MealDetialsIntents()
    data class changeChoices(val choices :Map<String,List<String>>): MealDetialsIntents()

    data class changePickupStatus(val pickupStatus:String):MealDetialsIntents()

    data class addMealToCart(val branchId: String,val mealId: String,val quantity: String,val sizeId: String,val choices :Map<String,List<String>>,val pickupStatus:String) : MealDetialsIntents()

}
