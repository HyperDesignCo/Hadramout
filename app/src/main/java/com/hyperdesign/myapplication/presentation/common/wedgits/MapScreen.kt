package com.hyperdesign.myapplication.presentation.common.wedgits

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.hyperdesign.myapplication.presentation.common.viewmodel.MapViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    onLocationSelected: (LatLng, String) -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (!permissionState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Location permission is required to show the map")
        }
        return
    }

    MapContent(onLocationSelected = onLocationSelected)
}

@Composable
private fun MapContent(
    onLocationSelected: (LatLng, String) -> Unit,
    mapViewModel: MapViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by mapViewModel.uiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position =CameraPosition.fromLatLngZoom(LatLng(51.5074, -0.1278), 10f) // San Francisco
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error ?: "An error occurred")
            }
            return
        }
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading map, please wait...")
            }
        } else {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(myLocationButtonEnabled = true),
                onMapLoaded = {
                    android.util.Log.d("MapScreen", "✅ Map loaded successfully!")
                },
                onMapClick = {
                    android.util.Log.d("MapScreen", "Map clicked at: $it")
                }
            ) {
                uiState.targetLatLng?.let {
                    com.google.maps.android.compose.Marker(
                        state = com.google.maps.android.compose.MarkerState(position = it),
                        title = "Selected Location"
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            var query by remember { mutableStateOf("") }
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    mapViewModel.searchPlaces(it)
                },
                placeholder = { Text("Where to go?") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            if (uiState.predictions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    items(uiState.predictions) { prediction ->
                        ListItem(
                            headlineContent = { Text(prediction.getPrimaryText(null).toString()) },
                            supportingContent = { Text(prediction.getSecondaryText(null).toString()) },
                            modifier = Modifier
                                .clickable {
                                    mapViewModel.selectPlace(prediction.placeId)
                                    query = prediction.getPrimaryText(null).toString()
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                val latLng = cameraPositionState.position.target
                scope.launch {
                    val address = mapViewModel.reverseGeocode(latLng)
                    onLocationSelected(latLng, address)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Confirm location")
        }
    }

    LaunchedEffect(uiState.targetLatLng) {
        uiState.targetLatLng?.let {
            android.util.Log.d("MapScreen", "Moving camera to: $it")
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    LaunchedEffect(Unit) {
        val googleApiAvailability = com.google.android.gms.common.GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (status != com.google.android.gms.common.ConnectionResult.SUCCESS) {
            android.util.Log.e("MapScreen", "Google Play Services error: $status")
            mapViewModel.updateErrorState("Google Play Services unavailable. Please update or install.")
        } else {
            android.util.Log.d("MapScreen", "Google Play Services available")
            mapViewModel.requestCurrentLocation(context)
        }
    }
}