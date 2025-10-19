package com.hyperdesign.myapplication.presentation.common.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.hyperdesign.myapplication.BuildConfig
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MapViewModel (
    private val placesClient: PlacesClient,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    @ApplicationContext private val context : Context
) : ViewModel() {

    data class UiState(
        val predictions: List<AutocompletePrediction> = emptyList(),
        val targetLatLng: LatLng? = null,
        val isLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var sessionToken: AutocompleteSessionToken? = null

    init {
        // Initialise Places once (you can also do it in the Activity)
        if (!Places.isInitialized()) {
            // You need the API key from BuildConfig – it is safe because the key is injected at build time
            Places.initialize(
                getApplication(context).applicationContext,
                BuildConfig.MAPS_API_KEY
            )
        }
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
                _uiState.update { it.copy(isLoading = false) }
                Log.e("MapVM", "Autocomplete error", e)
            }
        }
    }


    fun selectPlace(placeId: String) {
        viewModelScope.launch {
            val placeFields = listOf(Place.Field.LOCATION)
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()
            try {
                val response = placesClient.fetchPlace(request).await()
                response.place.location ?.let { latLng ->
                    _uiState.update { it.copy(targetLatLng = latLng, predictions = emptyList()) }
                }
            } catch (e: Exception) {
                Log.e("MapVM", "FetchPlace error", e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation(context: Context) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    _uiState.update { it.copy(targetLatLng = latLng) }
                }
            }
            .addOnFailureListener {
                // fallback – you could request a new location update
            }
    }


    suspend fun reverseGeocode(latLng: LatLng): String = withContext(Dispatchers.IO) {
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}