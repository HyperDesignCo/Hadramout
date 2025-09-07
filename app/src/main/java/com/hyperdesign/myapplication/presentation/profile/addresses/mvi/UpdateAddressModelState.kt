package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AreaResponseEntity
import com.hyperdesign.myapplication.domain.Entity.RegionResponseEntity

data class UpdateAddressModelState(
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val addressId: String = "",
    val regionId: Int = 0,
    val areaId: String = "",
    val district: String = "",
    val street: String = "",
    val phone: String = "",
    val secondPhoneNum: String = "",
    val buildingNumber: String = "",
    val floorNumber: String = "",
    val apartmentNumber: String = "",
    val specialSign: String = "",
    val extraInfo: String = "",
    val lat: String = "",
    val long: String = "",
    val regionError: String? = null,
    val areaError: String? = null,
    val updateAddressResponse: AddToCartResponseEntity? = null,
    val regions: RegionResponseEntity? = null,
    val area: AreaResponseEntity? = null
)