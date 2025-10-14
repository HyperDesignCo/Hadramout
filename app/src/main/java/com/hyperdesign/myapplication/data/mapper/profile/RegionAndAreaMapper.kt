package com.hyperdesign.myapplication.data.mapper.profile

import com.hyperdesign.myapplication.data.dto.AreaAndRegionDto
import com.hyperdesign.myapplication.data.dto.AreaResponseDto
import com.hyperdesign.myapplication.data.dto.RegionResponseDto
import com.hyperdesign.myapplication.domain.Entity.AreaAndRegionEntity
import com.hyperdesign.myapplication.domain.Entity.AreaResponseEntity
import com.hyperdesign.myapplication.domain.Entity.RegionResponseEntity


fun AreaResponseDto.toDomain(): AreaResponseEntity{
    return AreaResponseEntity(
        areas = areas.map { it.toDomain() }
    )
}


fun RegionResponseDto.toDomain(): RegionResponseEntity {
    return RegionResponseEntity(
        regions = regions?.map { it.toDomain() }
    )
}

fun AreaAndRegionDto.toDomain(): AreaAndRegionEntity{
    return AreaAndRegionEntity(
        id=id,
        name = name
    )
}

