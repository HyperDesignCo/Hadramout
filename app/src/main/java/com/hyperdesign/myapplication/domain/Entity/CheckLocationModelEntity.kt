package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName

data class CheckLocationResponseEntity(
    val status:String,
    val message:String,
    val data : LocationEntity

)

data class LocationEntity(

    val currentArea :String,
    val currentAreaNameEn :String,
    val currentAreaNameAr :String,
    val currentResturantBranch :String,
    val currentResturantBranchAr :String,
    val currentResturantBranchEn :String,
    val currentResturentBranchOpenTime:String?=null,
    val currentResturentBranchCloseTime:String?=null,
    val deliveryStatus :String,

    )

data class checkLocationRequest(
    val lat:String,
    val lng:String
)