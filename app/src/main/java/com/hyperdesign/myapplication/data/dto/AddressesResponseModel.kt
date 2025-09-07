package com.hyperdesign.myapplication.data.dto


data class AddressResponseDto(
    val addresses: List<AddressDto>
)


data class ShowAddressResponseDto (
    val address :AddressDto
)
data class AddressDto(
    val id: String,
    val region: RegionDto,
    val area: AreaDto,
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

data class RegionDto(
    val id: Int,
    val name: String
)

data class AreaDto(
    val id: Int,
    val name: String
)

