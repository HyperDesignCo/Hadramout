package com.hyperdesign.myapplication.presentation.common.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Geocoder
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.checkLocationRequest
import com.hyperdesign.myapplication.domain.usecase.home.CheckLocationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class MapViewModel(
    private val placesClient: PlacesClient,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val checkLocationUseCase: CheckLocationUseCase,
    val tokenManger: TokenManager,
    private val context: Context
) : ViewModel() {

    data class UiState(
        val predictions: List<AutocompletePrediction> = emptyList(),
        val targetLatLng: LatLng? = null,
        val detectedAddress: String? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private var sessionToken: AutocompleteSessionToken? = null
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)

    init {
        sessionToken = AutocompleteSessionToken.newInstance()
    }

    fun searchPlaces(query: String) {
        if (query.length < 2) {
            _uiState.update { it.copy(predictions = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setQuery(query)
                .build()

            try {
                val response = placesClient.findAutocompletePredictions(request).await()
                _uiState.update {
                    it.copy(predictions = response.autocompletePredictions, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                Log.e("MapViewModel", "Autocomplete error", e)
            }
        }
    }

    fun selectPlace(placeId: String) {
        viewModelScope.launch {
            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.LOCATION,
                Place.Field.FORMATTED_ADDRESS
            )
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()
            try {
                val response = placesClient.fetchPlace(request).await()
                val place: Place = response.place
                Log.d("MapViewModel", "Place fetched: ID=${place.id}, Name=${place.displayName}, LatLng=${place.location}, Address=${place.formattedAddress}")
                val latLng: LatLng? = place.location
                val address: String? = place.formattedAddress
                if (latLng != null) {
                    val finalAddress = address ?: reverseGeocode(latLng)
                    _uiState.update {
                        it.copy(
                            targetLatLng = latLng,
                            detectedAddress = finalAddress,
                            predictions = emptyList(),
                            isLoading = false
                        )
                    }
                    saveLocation(latLng)
                } else {
                    _uiState.update { it.copy(error = "Place has no location data", isLoading = false) }
                    Log.e("MapViewModel", "Place has no LatLng")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
                Log.e("MapViewModel", "FetchPlace error", e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(context: Context, callback: (LatLng) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val lastLocation = fusedLocationClient.lastLocation.await()
                if (lastLocation != null) {
                    val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                    val address = reverseGeocode(latLng)
                    _uiState.update { it.copy(targetLatLng = latLng, detectedAddress = address, isLoading = false) }
                    saveLocation(latLng)
                    Log.d("MapViewModel", "Current location set: $latLng")
                    callback(latLng)
                } else {
                    val currentLocation = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
                    if (currentLocation != null) {
                        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                        val address = reverseGeocode(latLng)
                        _uiState.update { it.copy(targetLatLng = latLng, detectedAddress = address, isLoading = false) }
                        saveLocation(latLng)
                        Log.d("MapViewModel", "Current location set (fallback): $latLng")
                        callback(latLng)
                    } else {
                        _uiState.update { it.copy(error = "Failed to get current location", isLoading = false) }
                        Log.e("MapViewModel", "Current location is null")
                        callback(LatLng(30.0444, 31.2357)) // Fallback to default
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to get current location: ${e.message}", isLoading = false) }
                Log.e("MapViewModel", "Failed to get current location", e)
                callback(LatLng(30.0444, 31.2357)) // Fallback to default
            }
        }
    }

    fun updateTargetLocation(latLng: LatLng) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val address = reverseGeocode(latLng)
            _uiState.update { it.copy(targetLatLng = latLng, detectedAddress = address, isLoading = false) }
            saveLocation(latLng)
            Log.d("MapViewModel", "Target location updated: $latLng, Address: $address")
        }
    }

    suspend fun reverseGeocode(latLng: LatLng): String = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale("ar"))
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
        } catch (e: Exception) {
            Log.e("MapViewModel", "Reverse geocode error", e)
            "Unknown location"
        }
    }

    fun checkLocationDelivery(context: Context, lat: String, lng: String, callback: (String, String?, String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = checkLocationRequest(lat, lng)
                val response = checkLocationUseCase(request)
                val deliveryStatus = response.data.deliveryStatus
                val currentRestaurantBranch = response.data.currentResturantBranch
                val areaId = response.data.currentArea
                callback(deliveryStatus, currentRestaurantBranch, areaId)
            } catch (e: Exception) {
                Log.e("MapViewModel", "Delivery check error", e)
                callback("0", null, "")
            }
        }
    }

    fun getCurrentRestaurantBranch(): String? {
        return sharedPreferences.getString("userResBranch", null)
    }

    fun saveLocation(latLng: LatLng) {
        sharedPreferences.edit {
            putFloat("latitude", latLng.latitude.toFloat())
            putFloat("longitude", latLng.longitude.toFloat())
        }
        Log.d("MapViewModel", "Saved location: lat=${latLng.latitude}, lng=${latLng.longitude}")
    }

    fun getSavedLocation(): LatLng? {
        val lat = sharedPreferences.getFloat("latitude", Float.MIN_VALUE).toDouble()
        val lng = sharedPreferences.getFloat("longitude", Float.MIN_VALUE).toDouble()
        return try {
            if (lat != Float.MIN_VALUE.toDouble() && lng != Float.MIN_VALUE.toDouble() &&
                lat in -90.0..90.0 && lng in -180.0..180.0) {
                LatLng(lat, lng)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MapViewModel", "Invalid saved location format: lat=$lat, lng=$lng", e)
            null
        }
    }

    fun updateErrorState(error: String) {
        _uiState.update { it.copy(error = error, isLoading = false) }
    }
}