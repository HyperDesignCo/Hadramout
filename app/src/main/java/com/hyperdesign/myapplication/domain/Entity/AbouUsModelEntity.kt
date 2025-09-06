package com.hyperdesign.myapplication.domain.Entity

data class AboutEntity(
    val mainAddress: String,
    val openingHoursBranches: String,
    val openingHoursDelivery: String,
    val aboutUs: String
)

data class SettingEntity(
    val hotline: String,
    val phone1: String,
    val contactEmail: String,
    val zero: String,
    val youtube: String,
    val twitter: String,
    val instgram: String,
    val tiktok: String,
    val deliveryTime: String,
    val minimumCharge: String,
    val serviceCharge: String,
    val vat: String,
    val phone2: String,
    val phone3: String
)

data class AboutUsResponseEntity(
    val about: AboutEntity,
    val setting: SettingEntity
)