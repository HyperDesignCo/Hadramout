package com.hyperdesign.myapplication.domain.Entity

import com.hyperdesign.myapplication.data.dto.AddressDto
import com.hyperdesign.myapplication.data.dto.AreaDto
import com.hyperdesign.myapplication.data.dto.RegionDto

data class AddressResponseEntity(
    val addresses: List<AddressEntity>
)

data class ShowAddressResponseEntity(
    val address :AddressEntity
)

data class AddressEntity(
    val id: String,
    val region: RegionEntity,
    val area: AreaEntity,
    val sub_region: String,
    val street: String,
    val special_sign: String,
    val building_number: String,
    val floor_number: String,
    val main_phone: String,
    val phone: String,
    val apartment_number: String,
    val extra_info: String,
    val latitude: String,
    val longitude: String
)

data class RegionEntity(
    val id: Int,
    val name: String
)

data class AreaEntity(
    val id: Int,
    val name: String
)
