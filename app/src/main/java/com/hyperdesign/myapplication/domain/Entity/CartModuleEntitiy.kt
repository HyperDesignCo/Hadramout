package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName


data class CartResponseEntity(
    val cart: CartEntity,
    val message: String
)
data class CartEntity(
    val id: String,
    val serviceCharge: Double,
    val deliveryCost: Double,
    val vat: Double,
    val primaryPrice: Double,
    val vatCost: Double,
    val serviceChargeCost: Double,
    val coupon: String,
    val couponDiscount: Double,
    val couponCost: Double,
    val offerDiscount: Double,
    val netPrice: Double,
    val totalPrice: Double,
    val specialRequests: String,
    val freeDelivery: Boolean,
    val cartMeals: List<CartMealEntity>
)

data class CartMealEntity(
    val id: String,
    val mealId: String,
    val mealTitle: String,
    val mealImage: String,
    val price: Double,
    val quantity: Int,
    val comment: String,
    val sizeId: String,
    val sizeTitle: String,
    val cartItemId: String,
    val netPrice: Double,
    val subChoicesPrice: Double,
    val primaryPrice: Double,
    val totalPrice: Double,
    val choices: List<String>
)

data class ShowCartRequest(
    @SerializedName("branch_id")
    val branchId: String
)