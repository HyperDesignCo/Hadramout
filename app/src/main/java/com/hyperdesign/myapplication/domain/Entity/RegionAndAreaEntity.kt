package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName

data class AreaAndRegionEntity(
    val id: Int,
    val name: String
)

data class AreaResponseEntity(
     val areas: List<AreaAndRegionEntity>
)

data class RegionResponseEntity(
    val regions: List<AreaAndRegionEntity>
)


data class CreateNewAddressRequest(
    val region_id: String,
    val area_id: String,
    val sub_region: String,
    val street: String,
    val main_phone: String,
    val phone: String,
    val building_number: String? = null,
    val floor_number: String? = null,
    val apartment_number: String? = null,
    val special_sign: String? = null,
    val extra_info: String? = null,
    val latitude: String,
    val longitude: String
)

data class DeleteAddressRequest(
    val id :String
)

data class updateAddressRequest(

    val id:String,
    val region_id: String,
    val area_id: String,
    val sub_region: String,
    val street: String,
    val main_phone: String,
    val phone: String,
    val building_number: String? = null,
    val floor_number: String? = null,
    val apartment_number: String? = null,
    val special_sign: String? = null,
    val extra_info: String? = null,
    val latitude: String,
    val longitude: String

)