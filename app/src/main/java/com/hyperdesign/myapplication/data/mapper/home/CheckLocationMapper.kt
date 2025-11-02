package com.hyperdesign.myapplication.data.mapper.home

import com.hyperdesign.myapplication.data.dto.CheckLocationResponseDto
import com.hyperdesign.myapplication.data.dto.LocationDto
import com.hyperdesign.myapplication.domain.Entity.CheckLocationResponseEntity
import com.hyperdesign.myapplication.domain.Entity.LocationEntity


fun CheckLocationResponseDto.toDomain() : CheckLocationResponseEntity{
    return CheckLocationResponseEntity(
        status = status,
        message=message,
        data=data.toDomain()
    )
}

fun LocationDto.toDomain(): LocationEntity{
    return LocationEntity(
        currentArea = currentArea,
        currentResturantBranch=currentResturantBranch,
        currentAreaNameAr = currentAreaNameAr,
        currentResturantBranchEn = currentResturantBranchEn,
        currentAreaNameEn = currentAreaNameEn,
        currentResturantBranchAr = currentResturantBranchAr,
        deliveryStatus = deliveryStatus,
        currentResturentBranchOpenTime = currentResturantBranchOpenTime,
        currentResturentBranchCloseTime = currentResturantBranchCloseTime
    )
}