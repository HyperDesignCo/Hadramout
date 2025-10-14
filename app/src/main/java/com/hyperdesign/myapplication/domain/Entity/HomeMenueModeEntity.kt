package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName

data class HomeResponse(
    val homeMenus: List<HomeMenu>,
    val bestSalesMeals: List<Meal>,
    val ads : List<AdsEntity>?,
    val slideshow:List<SlideShowEntity>?,
    val message: String
)

data class SlideShowEntity(
    val id:String,
    val title:String?,
    val text:String?,
    val image:String,
    @SerializedName("meal_id")
    val mealId:String?,
    @SerializedName("order_by")
    val orderId:String

)
data class AdsEntity(
    val id :String,
    val image:String,
    @SerializedName("meal_id")
    val mealId:String?,
    @SerializedName("order_by")
    val orderId:String
)

data class HomeMenu(
    val id: Int,
    val title: String,
    val meals: List<Meal>
)

data class Meal(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val price: Double,
    val discountPrice: Double?
)