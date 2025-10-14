package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.Entity.CreateNewAddressRequest
import com.hyperdesign.myapplication.domain.Entity.updateAddressRequest
import com.hyperdesign.myapplication.domain.usecase.profile.GetAreaUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.GetRegionsUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.ShowAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.UpdateAddressUseCase
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateAddressViewModel(
    private val showAddressUseCase: ShowAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val getRegionsUseCase: GetRegionsUseCase,
    private val getAreaUseCase: GetAreaUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _addressState = MutableStateFlow(UpdateAddressModelState())
    val addressState: StateFlow<UpdateAddressModelState> = _addressState.asStateFlow()

    private val _addressChannel = Channel<ValidationEvent>()
    val addressChannel = _addressChannel.receiveAsFlow()

    init {
        getRegions() // Fetch regions on initialization
    }

    fun handleIntents(intent: UpdateAddressIntents) {
        when (intent) {
            is UpdateAddressIntents.FetchAddress -> fetchAddress(intent.addressId)
            is UpdateAddressIntents.SaveUpdatedAddress -> saveUpdatedAddress(
                intent.regionId, intent.areaId, intent.district, intent.street,
                intent.firstPhoneNum, intent.secondPhoneNum, intent.buildingNum,
                intent.floorNum, intent.apartmentNum, intent.specialSign,
                intent.extraInfo, intent.lat, intent.long
            )
            is UpdateAddressIntents.ChangeApartmentNumValue -> _addressState.value = _addressState.value.copy(apartmentNumber = intent.apartmentNum)
            is UpdateAddressIntents.ChangeBuildingNumValue -> _addressState.value = _addressState.value.copy(buildingNumber = intent.buildingNum)
            is UpdateAddressIntents.ChangeDistrictValue -> _addressState.value = _addressState.value.copy(district = intent.district)
            is UpdateAddressIntents.ChangeExtraInfoValue -> _addressState.value = _addressState.value.copy(extraInfo = intent.extraInfo)
            is UpdateAddressIntents.ChangeFloorNumValue -> _addressState.value = _addressState.value.copy(floorNumber = intent.floorNum)
            is UpdateAddressIntents.ChangePhone1Value -> _addressState.value = _addressState.value.copy(phone = intent.phone1)
            is UpdateAddressIntents.ChangePhone2Value -> _addressState.value = _addressState.value.copy(secondPhoneNum = intent.phone2)
            is UpdateAddressIntents.ChangeSpecialSignValue -> _addressState.value = _addressState.value.copy(specialSign = intent.specialSign)
            is UpdateAddressIntents.ChangeStreetValue -> _addressState.value = _addressState.value.copy(street = intent.street)
            is UpdateAddressIntents.ChangeRegionId -> {
                val newRegionId = intent.regionId
                _addressState.value = _addressState.value.copy(
                    regionId = newRegionId,
                    regionError = if (newRegionId == 0) "Region is required" else null
                )
                if (newRegionId != 0) {
                    getAreaByRegionId(newRegionId)
                }
            }
            is UpdateAddressIntents.ChangeAreaId -> _addressState.value = _addressState.value.copy(
                areaId = intent.areaId.toString(),
                areaError = if (intent.areaId == 0) "Area is required" else null
            )
            UpdateAddressIntents.GetRegions -> getRegions()
        }
    }

    private fun fetchAddress(addressId: String) {
        _addressState.value = _addressState.value.copy(isLoading = true, addressId = addressId)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = showAddressUseCase(addressId.toInt())
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
//                    regionId = null,
                    areaId = response.address.area.id.toString(),
                    district = response.address.sub_region,
                    street = response.address.street,
                    phone = response.address.main_phone,
                    secondPhoneNum = response.address.phone,
                    buildingNumber = response.address.building_number,
                    floorNumber = response.address.floor_number,
                    apartmentNumber = response.address.apartment_number,
                    specialSign = response.address.special_sign,
                    extraInfo = response.address.extra_info,
                    lat = response.address.latitude,
                    long = response.address.longitude
                )
                // Fetch areas for the selected region after fetching address
//                getAreaByRegionId(response.address.region?.id!!)
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                _addressChannel.send(ValidationEvent.Failure(it.message ?: "Failed to fetch address"))
                Log.e("UpdateAddressViewModel", "Fetch failed: ${it.message}")
            }
        }
    }

    private fun saveUpdatedAddress(
        regionId: String, areaId: String, district: String, street: String,
        firstPhoneNum: String, secondPhoneNum: String, buildingNum: String,
        floorNum: String, apartmentNum: String, specialSign: String,
        extraInfo: String, lat: String, long: String
    ) {
        _addressState.value = _addressState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val request = updateAddressRequest(
                    id = _addressState.value.addressId,
                    region_id = regionId,
                    area_id = areaId,
                    sub_region = district,
                    street = street,
                    main_phone = firstPhoneNum,
                    phone = secondPhoneNum,
                    building_number = buildingNum,
                    floor_number = floorNum,
                    apartment_number = apartmentNum,
                    special_sign = specialSign,
                    extra_info = extraInfo,
                    latitude = lat,
                    longitude = long
                )
                val response = updateAddressUseCase(request)
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    updateAddressResponse = response
                )
                if (response.message == "address added successfully") {
                    _addressChannel.send(ValidationEvent.Success)
                } else {
                    _addressChannel.send(ValidationEvent.Failure(response.message))
                }
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                _addressChannel.send(ValidationEvent.Failure(it.message ?: "Failed to update address"))
                Log.e("UpdateAddressViewModel", "Update failed: ${it.message}")
            }
        }
    }

    private fun getRegions() {
        _addressState.value = _addressState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getRegionsUseCase() // Assuming ShowAddressUseCase has this method
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    regions = response
                )
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("UpdateAddressViewModel", "Get regions failed: ${it.message}")
            }
        }
    }

    private fun getAreaByRegionId(regionId: Int) {
        _addressState.value = _addressState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getAreaUseCase(regionId) // Assuming ShowAddressUseCase has this method
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    area = response
                )
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("UpdateAddressViewModel", "Get areas failed: ${it.message}")
            }
        }
    }
}