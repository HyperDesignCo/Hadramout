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
import org.koin.androidx.viewmodel.ext.android.viewModel
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
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    onLocationSelected: (LatLng, String) -> Unit   // callback when the user confirms a location
) {
    // 1. Permission handling (Accompanist makes it easy)
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
        // Show a friendly message while waiting for permission
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Location permission is required to show the map")
        }
        return
    }

    // Permission granted → show the map UI
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

    // GoogleMap composable (needs a GoogleMapOptions with the API key)
    val cameraPositionState = rememberCameraPositionState {
        // start somewhere – will be overwritten by current location
        position = CameraPosition.fromLatLngZoom(LatLng(24.7136, 46.6753), 10f)
    }


    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true)
        ) {
            // optional: you can add markers here
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
//                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )


            if (uiState.predictions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 200.dp)
                        .background(Color.White)
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
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }


    LaunchedEffect(Unit) {
        mapViewModel.requestCurrentLocation(context)
    }
}