package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class CheckOutResponseDto(
    @SerializedName("branche")
    val branch: BranchInfoDTO?,
    val cart: CartDto,
    val message: String,
    val addresses: List<AddressDto>,
    val payment_methods: List<PaymentMethodDto>
)



data class PaymentMethodDto(
    val id: String,
    val title: String,
    val image: String,
    val show_android: String,
    val show_apple: String
)

data class BranchInfoDTO(
    val id :Int,
    @SerializedName("title_ar")
    val titleAr :String?,
    @SerializedName("title_en")
    val titleEn :String?,
    val slug :String?,
    @SerializedName("address_en")
    val addressEn :String?,
    @SerializedName("address_ar")
    val addressAr :String?,
    @SerializedName("delivery_cost")
    val deliveryCost :String?,
    val lat :String?,
    val lng :String?,
    val status:Int?,
    @SerializedName("busy_hours")
    val busyHours:Int?,
    @SerializedName("busy_end_at")
    val busyEndAt :String?,
    @SerializedName("open_time")
    val openTime:String?,
    @SerializedName("close_time")
    val closeTime:String?,
    @SerializedName("pickup_status")
    val pickupStatus:Int?,
    @SerializedName("pickup_busy_hours")
    val pickupBusyHours:String?,
    @SerializedName("created_at")
    val createAt:String?,
    @SerializedName("updated_at")
    val updateAt:String?,
    @SerializedName("deleted_at")
    val deleteAt:String?,
)