package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AreaResponseEntity
import com.hyperdesign.myapplication.domain.Entity.RegionResponseEntity

data class AddressesModelState(
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val addresses: AddressResponseEntity? = null,
//    val regions: RegionResponseEntity? = null,
    val area: AreaResponseEntity? = null,
//    val regionIdValue: String = "",
//    val regionError: String? = null,
    val areaIdValue: String = "",
    val areaId: String = "",
//    val areaError: String? = null,
    val district: String = "",
    val districtError: String? = null,
    val street: String = "",
    val streetError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val buildingNumber: String = "",
    val floorNumber: String = "",
    val apartmentNumber: String = "",
    val specialSign: String = "",
    val extraInfo: String = "",
    val regionId: Int = 0,
    val secondPhoneNum: String = "",
    val secondPhoneNumError:String?=null,
    val createNewAddressRespnse: AddToCartResponseEntity?=null,
    val deleteAddressRespnse: AddToCartResponseEntity?=null,
    var lat: String = "", // Added latitude
    var long: String = "" // Added longitude
)