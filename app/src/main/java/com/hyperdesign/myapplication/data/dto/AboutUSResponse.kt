package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class AboutUsResponseDTO(
    @SerializedName("about")
    val about: AboutDTO?,

    @SerializedName("setting")
    val setting: SettingDTO?
)

data class AboutDTO(
    @SerializedName("main_address")
    val mainAddress: String?,

    @SerializedName("opening_hours_branches")
    val openingHoursBranches: String?,

    @SerializedName("opening_hours_delivery")
    val openingHoursDelivery: String?,

    @SerializedName("about_us")
    val aboutUs: String?
)

data class SettingDTO(
    @SerializedName("hotline")
    val hotline: String?,

    @SerializedName("phone1")
    val phone1: String?,

    @SerializedName("contact_email")
    val contactEmail: String?,

    @SerializedName("0")
    val zero: String?,

    @SerializedName("youtube")
    val youtube: String?,

    @SerializedName("twitter")
    val twitter: String?,

    @SerializedName("instgram")
    val instgram: String?,

    @SerializedName("tiktok")
    val tiktok: String?,

    @SerializedName("delivery_time")
    val deliveryTime: String?,

    @SerializedName("minimum_charge")
    val minimumCharge: String?,

    @SerializedName("service_charge")
    val serviceCharge: String?,

    @SerializedName("vat")
    val vat: String?,

    @SerializedName("phone2")
    val phone2: String?,

    @SerializedName("phone3")
    val phone3: String?
)