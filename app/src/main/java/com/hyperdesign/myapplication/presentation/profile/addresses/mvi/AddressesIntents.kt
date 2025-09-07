package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

sealed class AddressesIntents {

    data object GetAddress : AddressesIntents()

    data object GetRegions : AddressesIntents()

    data class GetArea(val regionId: Int) : AddressesIntents()

    data class SaveNewAddress(
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
    ) : AddressesIntents()

    data class ChangeDistrictValue(val district: String) : AddressesIntents()

    data class ChangeStreetValue(val street: String) : AddressesIntents()

    data class ChangePhone1Value(val phone1: String) : AddressesIntents()

    data class ChangePhone2Value(val phone2: String) : AddressesIntents()

    data class ChangeBuildingNumValue(val buildingNum: String) : AddressesIntents()

    data class ChangeFloorNumValue(val floorNum: String) : AddressesIntents()

    data class ChangeApartmentNumValue(val apartmentNum: String) : AddressesIntents()

    data class ChangeSpecialSignValue(val specialSign: String) : AddressesIntents()

    data class ChangeExtraInfoValue(val extraInfo: String) : AddressesIntents()

    data class ChangeRegionId(val regionId: Int) : AddressesIntents()

    data class ChangeAreaId(val areaId: Int) : AddressesIntents()

    data class DeleteAddress(val id:String):AddressesIntents()
}