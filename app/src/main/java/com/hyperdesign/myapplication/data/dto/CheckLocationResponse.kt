package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class CheckLocationResponseDto(
    val status:String,
    val message:String,
    val data : LocationDto

)

data class LocationDto(
    @SerializedName("current_area")
    val currentArea :String,
    @SerializedName("current_area_name_en")
    val currentAreaNameEn :String,
    @SerializedName("current_area_name_ar")
    val currentAreaNameAr :String,
    @SerializedName("current_resturant_branch")
    val currentResturantBranch :String,
    @SerializedName("current_resturant_branch_ar")
    val currentResturantBranchAr :String,
    @SerializedName("current_resturant_branch_en")
    val currentResturantBranchEn :String,
    @SerializedName("delivery_status")
    val deliveryStatus :String,


)