package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.CreateNewAddressRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteAddressRequest
import com.hyperdesign.myapplication.domain.usecase.home.GetAllAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.CreateNewAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.DeleteAddressUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.GetAreaUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.GetRegionsUseCase
import com.hyperdesign.myapplication.presentation.utilies.ValidatePhoneNumber
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddressesViewModel @Inject constructor(
    private val getAllAddressUseCase: GetAllAddressUseCase,
    private val getRegionsUseCase: GetRegionsUseCase,
    private val getAreaUseCase: GetAreaUseCase,
    private val validateText: ValidateText,
    private val validatePhoneNumber: ValidatePhoneNumber,
    @ApplicationContext private val context: Context,
    private val createNewAddressUseCase: CreateNewAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    val tokenManager: TokenManager
) : ViewModel() {

    private val _addressState = MutableStateFlow(AddressesModelState())
    val addressState: StateFlow<AddressesModelState> = _addressState.asStateFlow()

    private val _addressChannel = Channel<ValidationEvent>()
    val addressChannel = _addressChannel.receiveAsFlow()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    init {
        _addressState.update { it->
            it.copy(phone = tokenManager.getUserData()?.mobile.orEmpty())
        }
        getAllAddress()
//        getRegions()
    }

    fun handleIntents(intent: AddressesIntents) {
        when (intent) {
            AddressesIntents.GetAddress -> getAllAddress()
            is AddressesIntents.GetArea -> getAreaByRegionId(intent.regionId)
            AddressesIntents.GetRegions ->{
//                getRegions()
            }
            is AddressesIntents.SaveNewAddress -> saveNewAddress(intent.lat, intent.long)
            is AddressesIntents.ChangeApartmentNumValue -> _addressState.value = _addressState.value.copy(apartmentNumber = intent.apartmentNum)
            is AddressesIntents.ChangeBuildingNumValue -> _addressState.value = _addressState.value.copy(buildingNumber = intent.buildingNum)
            is AddressesIntents.ChangeDistrictValue -> _addressState.value = _addressState.value.copy(district = intent.district)
            is AddressesIntents.ChangeExtraInfoValue -> _addressState.value = _addressState.value.copy(extraInfo = intent.extraInfo)
            is AddressesIntents.ChangeFloorNumValue -> _addressState.value = _addressState.value.copy(floorNumber = intent.floorNum)
            is AddressesIntents.ChangePhone1Value ->{ _addressState.value =
                _addressState.value.copy(phone = intent.phone1, phoneError = null)
            }
            is AddressesIntents.ChangePhone2Value ->{
                _addressState.value = _addressState.value.copy(secondPhoneNum = intent.phone2, secondPhoneNumError = null)
            }
            is AddressesIntents.ChangeSpecialSignValue -> _addressState.value = _addressState.value.copy(specialSign = intent.specialSign)
            is AddressesIntents.ChangeStreetValue -> {
                _addressState.value = _addressState.value.copy(
                    street = intent.street,
                    streetError = null
                )
            }
            is AddressesIntents.ChangeRegionId -> {
//                val newRegionId = intent.regionId
//                _addressState.value = _addressState.value.copy(
//                    regionId = newRegionId,
//                    regionError = if (newRegionId == 0) context.getString(R.string.region_is_requierd) else null
//                )
//                if (newRegionId != 0) {
//                    getAreaByRegionId(newRegionId)
//                }
            }
            is AddressesIntents.ChangeAreaId -> _addressState.value = _addressState.value.copy(
                areaId = intent.areaId.toString(),
//                areaError = if (intent.areaId == 0) context.getString(R.string.area_is_requied) else null
            )
            is AddressesIntents.DeleteAddress -> deleteAddress(intent.id)
        }
    }

    private fun deleteAddress(id: String) {
        _addressState.value = _addressState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val deleteRequest = DeleteAddressRequest(id)
                val response = deleteAddressUseCase(deleteRequest)
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    deleteAddressRespnse = response
                )
                Log.d("AddressesViewModel", "Delete response: ${response?.message}")
                if (response.message == "address deleted successfully") {
                    _addressChannel.send(ValidationEvent.Success)
                } else {
                    _addressChannel.send(ValidationEvent.Failure(response?.message ?: "Unknown error"))
                }
                // Reset deleteAddressResponse after processing to trigger state change
                _addressState.value = _addressState.value.copy(deleteAddressRespnse = null)
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    deleteAddressRespnse = null,
                    errorMsg = it.message
                )
                _addressChannel.send(ValidationEvent.Failure(it.message ?: "Unknown error"))
                Log.e("AddressesViewModel", "Delete failed: ${it.message}")
            }
        }
    }

    fun fetchCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val location: Location? = fusedLocationClient.lastLocation.await()
                location?.let {
                    _addressState.value = _addressState.value.copy(
                        lat = it.latitude.toString(),
                        long = it.longitude.toString()
                    )
                } ?: run {
                    _addressState.value = _addressState.value.copy(
                        errorMsg = "Location not available"
                    )
                    Log.w("Location", "No last known location available")
                }
            } catch (e: SecurityException) {
                _addressState.value = _addressState.value.copy(
                    errorMsg = "Location permission denied"
                )
                Log.e("LocationError", "SecurityException: ${e.message}")
            } catch (e: Exception) {
                _addressState.value = _addressState.value.copy(
                    errorMsg = "Failed to get location: ${e.message}"
                )
                Log.e("LocationError", e.message.toString())
            }
        }
    }

    private fun saveNewAddress(lat: String, long: String) {
//        val areaResult = validateText.execute(_addressState.value.areaId)
//        val districtResult = validateText.execute(_addressState.value.district)
        val streetValue = validateText.execute(_addressState.value.street)
        val phoneNum = validatePhoneNumber.execute(_addressState.value.phone)
        val secondNum =  validatePhoneNumber.execute(_addressState.value.secondPhoneNum)
        val hasError = listOf( streetValue, secondNum,phoneNum).any { !it.successful }

//        val areaIdError = if (_addressState.value.areaId.isEmpty() || _addressState.value.areaId == "0") context.getString(R.string.area_is_requied) else null
//        val districtError = if (districtResult.successful) null else context.getString(R.string.your_district_required)
        val streetError = if (streetValue.successful) null else context.getString(R.string.your_street_requied)
        val phoneError = if (phoneNum.successful) null else context.getString(R.string.invalid_phone_number_format)
        val secondPhoneError = if (secondNum.successful) null else context.getString(R.string.invalid_phone_number_format)

        if (hasError) {
            _addressState.value = _addressState.value.copy(
//                districtError = districtError,
                streetError = streetError,
                phoneError = phoneError,
                secondPhoneNumError = secondPhoneError
            )
            return
        }

        _addressState.value = _addressState.value.copy(
            isLoading = true,
            streetError=null,
            phoneError = null,
            secondPhoneNumError = null
        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val createNewAddressRequest = CreateNewAddressRequest(
                    area_id = _addressState.value.areaId,
                    street = _addressState.value.street,
                    main_phone = _addressState.value.phone,
                    building_number = _addressState.value.buildingNumber,
                    floor_number = _addressState.value.floorNumber,
                    phone = _addressState.value.secondPhoneNum,
                    apartment_number = _addressState.value.apartmentNumber,
                    special_sign = _addressState.value.specialSign,
                    extra_info = _addressState.value.extraInfo,
                    latitude = lat,
                    longitude = long
                )
                val response = createNewAddressUseCase(createNewAddressRequest)
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    createNewAddressRespnse = response
                )
                if (response.message == "address added successfully") {
                    _addressChannel.send(ValidationEvent.Success)
                } else {
                    _addressChannel.send(ValidationEvent.Failure(response.message))
                }
            }.onSuccess {
                _addressState.value = _addressState.value.copy(isLoading = false)
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                _addressChannel.send(ValidationEvent.Failure(it.message.toString()))
            }
        }
    }

    private fun getAreaByRegionId(regionId: Int) {
        _addressState.value = _addressState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getAreaUseCase(regionId = regionId)
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    area = response
                )
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("failed to get area", it.message.toString())
            }
        }
    }

//    private fun getRegions() {
//        _addressState.value = _addressState.value.copy(isLoading = true)
//        viewModelScope.launch(Dispatchers.IO) {
//            runCatching {
//                val response = getRegionsUseCase()
//                _addressState.value = _addressState.value.copy(
//                    isLoading = false,
//                    regions = response
//                )
//            }.onFailure {
//                _addressState.value = _addressState.value.copy(
//                    isLoading = false,
//                    errorMsg = it.message
//                )
//                Log.e("failed to get regions", it.message.toString())
//            }
//        }
//    }

    private fun getAllAddress() {
        _addressState.value = _addressState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getAllAddressUseCase()
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    addresses = response
                )
            }.onFailure {
                _addressState.value = _addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("failed to get address", it.message.toString())
            }
        }
    }
}