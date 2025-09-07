package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class AreaAndRegionDto(
    val id: Int,
    val name: String
)

data class AreaResponseDto(
    @SerializedName("areas") val areas: List<AreaAndRegionDto>
)

data class RegionResponseDto(
    @SerializedName("regions") val regions: List<AreaAndRegionDto>
)