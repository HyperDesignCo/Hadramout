package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

sealed class UpdateAddressIntents {

    data class FetchAddress(val addressId: String) : UpdateAddressIntents()

    data object GetRegions : UpdateAddressIntents()

    data class SaveUpdatedAddress(
        val regionId: String,
        val areaId: String,
        val district: String,
        val street: String,
        val firstPhoneNum: String,
        val secondPhoneNum: String,
        val buildingNum: String,
        val floorNum: String,
        val apartmentNum: String,
        val specialSign: String,
        val extraInfo: String,
        val lat: String,
        val long: String
    ) : UpdateAddressIntents()

    data class ChangeDistrictValue(val district: String) : UpdateAddressIntents()
    data class ChangeStreetValue(val street: String) : UpdateAddressIntents()
    data class ChangePhone1Value(val phone1: String) : UpdateAddressIntents()
    data class ChangePhone2Value(val phone2: String) : UpdateAddressIntents()
    data class ChangeBuildingNumValue(val buildingNum: String) : UpdateAddressIntents()
    data class ChangeFloorNumValue(val floorNum: String) : UpdateAddressIntents()
    data class ChangeApartmentNumValue(val apartmentNum: String) : UpdateAddressIntents()
    data class ChangeSpecialSignValue(val specialSign: String) : UpdateAddressIntents()
    data class ChangeExtraInfoValue(val extraInfo: String) : UpdateAddressIntents()
    data class ChangeRegionId(val regionId: Int) : UpdateAddressIntents()
    data class ChangeAreaId(val areaId: Int) : UpdateAddressIntents()
}