package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class CartResponseDto(
    val cart: CartDto?, // Made nullable to handle cart=null case
    @SerializedName("delivery_time")
    val deliveryTime:String?=null,
    @SerializedName("cross_selling_meal")
    val crossSellingMeal : List<SellingMealDto>?=null,
    @SerializedName("up_selling_meal")
    val upSellingMeal: List<SellingMealDto>?=null,
    val message: String
)


data class SellingMealDto(
    val id :String,
    val title:String,
    val description:String,
    val image:String,
    val price:String,
    @SerializedName("discount_price")
    val discountPrice:String?=null
)
data class CartDto(
    val id: String,
    @SerializedName("service_charge")
    val serviceCharge: String,

    @SerializedName("delivery_cost")
    val deliveryCost: String,
    val vat: String,
    @SerializedName("primary_price")
    val primaryPrice: String,
    @SerializedName("vat_cost")
    val vatCost: String,
    @SerializedName("service_charge_cost")
    val serviceChargeCost: String,
    val coupon: String,
    @SerializedName("coupon_discount")
    val couponDiscount: String,
    @SerializedName("coupon_cost")
    val couponCost: String,
    @SerializedName("offer_discount")
    val offerDiscount: String,
    @SerializedName("net_price")
    val netPrice: String?,
    @SerializedName("total_price")
    val totalPrice: String,
    @SerializedName("special_requests")
    val specialRequests: String,
    @SerializedName("free_delivery")
    val freeDelivery: String,
    @SerializedName("cart_meals")
    val cartMeals: List<CartMealDto>?,
    @SerializedName("pickup_status")
    val pickupStatus:String,
)

data class CartMealDto(
    val id: String,
    @SerializedName("meal_id")
    val mealId: String,
    @SerializedName("meal_title")
    val mealTitle: String,
    @SerializedName("meal_image")
    val mealImage: String,
    val price: String,
    val quantity: String,
    val comment: String,
    @SerializedName("size_id")
    val sizeId: String,
    @SerializedName("size_title")
    val sizeTitle: String,
    @SerializedName("cart_item_id")
    val cartItemId: String,
    @SerializedName("sub_choices_price")
    val subChoicesPrice: String,
    @SerializedName("primary_price")
    val primaryPrice: String,
    @SerializedName("net_price")
    val netPrice: String?, // Made nullable to match API response
    @SerializedName("total_price")
    val totalPrice: String,
    val choices: List<String>?
)