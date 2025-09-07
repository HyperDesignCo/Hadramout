package com.hyperdesign.myapplication.data.mapper.home

import com.hyperdesign.myapplication.data.dto.AddressDto
import com.hyperdesign.myapplication.data.dto.AddressResponseDto
import com.hyperdesign.myapplication.data.dto.AreaDto
import com.hyperdesign.myapplication.data.dto.RegionDto
import com.hyperdesign.myapplication.data.dto.ShowAddressResponseDto
import com.hyperdesign.myapplication.domain.Entity.AddressEntity
import com.hyperdesign.myapplication.domain.Entity.AddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AreaEntity
import com.hyperdesign.myapplication.domain.Entity.RegionEntity
import com.hyperdesign.myapplication.domain.Entity.ShowAddressResponseEntity

fun AddressResponseDto.toDomain() : AddressResponseEntity {
    return AddressResponseEntity(
        addresses = addresses.map { it.toDomain() }
    )

}

fun ShowAddressResponseDto.toDomain(): ShowAddressResponseEntity{
    return ShowAddressResponseEntity(
        address = address.toDomain()
    )
}
fun AddressDto.toDomain(): AddressEntity{

    return AddressEntity(
        id = id,
        region = region.toDomain(),
        area = area.toDomain(),
        sub_region = sub_region,
        street = street,
        special_sign = special_sign,
        building_number = building_number,
        floor_number = floor_number,
        main_phone = main_phone,
        phone = phone,
        apartment_number = apartment_number,
        extra_info = extra_info,
        latitude = latitude,
        longitude = longitude

    )

}

fun RegionDto.toDomain(): RegionEntity{
    return RegionEntity(
        name = name,
        id = id
    )
}

fun AreaDto.toDomain(): AreaEntity{
    return AreaEntity(
        name = name,
        id = id
    )
}