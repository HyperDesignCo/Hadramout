package com.hyperdesign.myapplication.data.mapper.profile.setting

import com.hyperdesign.myapplication.data.dto.AboutDTO
import com.hyperdesign.myapplication.data.dto.AboutUsResponseDTO
import com.hyperdesign.myapplication.data.dto.SettingDTO
import com.hyperdesign.myapplication.domain.Entity.AboutEntity
import com.hyperdesign.myapplication.domain.Entity.AboutUsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.SettingEntity

fun AboutUsResponseDTO.toEntity(): AboutUsResponseEntity {
    return AboutUsResponseEntity(
        about = about?.toEntity() ?: AboutEntity(
            mainAddress = "",
            openingHoursBranches = "",
            openingHoursDelivery = "",
            aboutUs = ""
        ),
        setting = setting?.toEntity() ?: SettingEntity(
            hotline = "",
            phone1 = "",
            contactEmail = "",
            zero = "",
            youtube = "",
            twitter = "",
            instgram = "",
            tiktok = "",
            deliveryTime = "",
            minimumCharge = "",
            serviceCharge = "",
            vat = "",
            phone2 = "",
            phone3 = ""
        )
    )
}

fun AboutDTO.toEntity(): AboutEntity {
    return AboutEntity(
        mainAddress = mainAddress ?: "",
        openingHoursBranches = openingHoursBranches ?: "",
        openingHoursDelivery = openingHoursDelivery ?: "",
        aboutUs = aboutUs ?: ""
    )
}

fun SettingDTO.toEntity(): SettingEntity {
    return SettingEntity(
        hotline = hotline ?: "",
        phone1 = phone1 ?: "",
        contactEmail = contactEmail ?: "",
        zero = zero ?: "",
        youtube = youtube ?: "",
        twitter = twitter ?: "",
        instgram = instgram ?: "",
        tiktok = tiktok ?: "",
        deliveryTime = deliveryTime ?: "",
        minimumCharge = minimumCharge ?: "",
        serviceCharge = serviceCharge ?: "",
        vat = vat ?: "",
        phone2 = phone2 ?: "",
        phone3 = phone3 ?: ""
    )
}