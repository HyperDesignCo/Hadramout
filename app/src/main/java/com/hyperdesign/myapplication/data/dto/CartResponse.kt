package com.hyperdesign.myapplication.data.dto



data class CartResponseDto(
    val cart: CartDto,
    val message: String
)

data class CartDto(
    val id: String,

    val serviceCharge: String,
    val deliveryCost: String,
    val vat: String,
    val primaryPrice: String,
    val vatCost: String,
    val serviceChargeCost: String,
    val coupon: String,
    val couponDiscount: String,
    val couponCost: String,
    val offerDiscount: String,
    val netPrice: String,
    val totalPrice: String,
    val specialRequests: String,
    val freeDelivery: String,
    val cartMeals: List<CartMealDto>
)

data class CartMealDto(
    val id: String,
    val mealId: String,
    val mealTitle: String,
    val mealImage: String,
    val price: String,
    val quantity: String,
    val comment: String,
    val sizeId: String,
    val sizeTitle: String,
    val cartItemId: String,
    val subChoicesPrice: String,
    val primaryPrice: String,
    val totalPrice: String,
    val choices: List<String>
)