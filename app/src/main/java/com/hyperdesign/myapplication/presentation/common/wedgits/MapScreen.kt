package com.hyperdesign.myapplication.presentation.common.wedgits

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.hyperdesign.myapplication.R
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.hyperdesign.myapplication.presentation.common.viewmodel.MapViewModel
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navigateFrom: String = "",
    mapViewModel: MapViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    Log.d("MapScreen", "Received navigateFrom: $navigateFrom")
    val scope = rememberCoroutineScope()
    val uiState by mapViewModel.uiState.collectAsState()
    val defaultZoom = 15f
    val defaultLatLng = LatLng(30.0444, 31.2357) // Default to Cairo
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, defaultZoom)
    }
    val density = LocalDensity.current
    var showNoDeliveryDialog by remember { mutableStateOf(false) }
    var showDiffBranchDialog by remember { mutableStateOf(false) }
    var isInitialLocationSet by remember { mutableStateOf(false) }

    // Load saved location on startup
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        } else {
            // Check for saved location first
            val savedLocation = mapViewModel.getSavedLocation()
            if (savedLocation != null) {
                Log.d("MapScreen", "Loaded saved location: $savedLocation")
                cameraPositionState.position = CameraPosition.fromLatLngZoom(savedLocation, defaultZoom)
                mapViewModel.updateTargetLocation(savedLocation)
                isInitialLocationSet = true
            } else {
                Log.d("MapScreen", "No saved location, requesting current location")
                mapViewModel.requestCurrentLocation(context)
            }
        }
    }

    LaunchedEffect(uiState.targetLatLng) {
        uiState.targetLatLng?.let { latLng ->
            if (!isInitialLocationSet) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom)
                )
                isInitialLocationSet = true
            } else {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(latLng, cameraPositionState.position.zoom)
                )
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            delay(300)
            val target = cameraPositionState.position.target
            mapViewModel.updateTargetLocation(target)
            Log.d("MapScreen", "Camera idle, updated location: $target")
        }
    }

    if (!permissionState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Location permission is required to show the map")
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            onMapLoaded = {
                Log.d("MapScreen", "✅ Map loaded successfully!")
            },
            onMapClick = { latLng ->
                mapViewModel.updateTargetLocation(latLng)
                Log.d("MapScreen", "Map clicked, updated location: $latLng")
            }
        )

        Image(
            painter = painterResource(R.drawable.mark),
            contentDescription = "Fixed Marker",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-16).dp)
        )

        // Search bar and predictions (commented out as in original code)
        /*
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .background(Color.White, RoundedCornerShape(25.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .width(300.dp)
        ) {
            var query by remember { mutableStateOf("") }
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    mapViewModel.searchPlaces(it)
                },
                placeholder = { Text(stringResource(R.string.search_for_an_location)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                }
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
        */

        uiState.detectedAddress?.let { address ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.selected_location_is),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = address,
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        Button(
            onClick = {
                uiState.targetLatLng?.let { latLng ->
                    scope.launch {
                        Log.d("MapScreen", "Choose Location clicked, latLng: $latLng")
                        val address = mapViewModel.reverseGeocode(latLng)
                        Log.d("MapScreen", "Reverse geocoded address: $address")
                        mapViewModel.checkLocationDelivery(
                            context,
                            latLng.latitude.toString(),
                            latLng.longitude.toString()
                        ) { deliveryStatus, currentRestaurantBranch ->
                            Log.d("MapScreen", "checkLocationDelivery result: deliveryStatus=$deliveryStatus, currentRestaurantBranch=$currentRestaurantBranch")
                            Log.d("MapScreen", "Stored Current restaurant branch: ${mapViewModel.tokenManger.getCurrentResturentBranch()}")
                            when (deliveryStatus) {
                                "1" -> {
                                    if (currentRestaurantBranch == null || mapViewModel.tokenManger.getCurrentResturentBranch() == currentRestaurantBranch) {
                                        mapViewModel.saveLocation(latLng)
                                        Log.d("MapScreen", "Location saved: lat=${latLng.latitude}, lng=${latLng.longitude}")
                                        val bundle = Bundle().apply {
                                            putString("selectedLocation", address)
                                            putString("lat", latLng.latitude.toString())
                                            putString("lng", latLng.longitude.toString())
                                        }
                                        if (navigateFrom == "Home") {
                                            val route = "home_screen?lat=${latLng.latitude}&lng=${latLng.longitude}&pickup="
                                            Log.d("MapScreen", "Navigating to: $route")
                                            try {
                                                navController.navigate(route) {
                                                    popUpTo(Screen.MapScreen.route) { inclusive = true }
                                                }
                                                Log.d("MapScreen", "Navigation successful")
                                            } catch (e: Exception) {
                                                Log.e("MapScreen", "Navigation failed: ${e.message}", e)
                                                Toast.makeText(context, "Navigation failed: ${e.message}", Toast.LENGTH_LONG).show()
                                            }
                                        } else {
                                            Log.w("MapScreen", "Navigation not implemented for navigateFrom=$navigateFrom")
                                            Toast.makeText(context, "Navigation not implemented for $navigateFrom", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Log.d("MapScreen", "Branch mismatch, showing DiffBranchDialog")
                                        showDiffBranchDialog = true
                                    }
                                }
                                "0" -> {
                                    Log.d("MapScreen", "No delivery available, showing NoDeliveryDialog")
                                    showNoDeliveryDialog = true
                                }
                                else -> {
                                    Log.w("MapScreen", "Unknown delivery status: $deliveryStatus")
                                    showNoDeliveryDialog = true
                                }
                            }
                        }
                    }
                } ?: run {
                    Log.w("MapScreen", "No location selected")
                    Toast.makeText(context, "Please select a location on the map", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15A25))
        ) {
            Text(
                text = stringResource(R.string.choose_location),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (showNoDeliveryDialog) {
            NoDeliveryDialog(
                message = stringResource(R.string.change_location_not_work),
                onDismiss = { showNoDeliveryDialog = false },
                onPickupClick = {
                    showNoDeliveryDialog = false
                    val route = "home_screen?pickup=1"
                    Log.d("MapScreen", "Navigating to Home with pickup: $route")
                    try {
                        navController.navigate(route) {
                            popUpTo(Screen.MapScreen.route) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Log.e("MapScreen", "Pickup navigation failed: ${e.message}", e)
                        Toast.makeText(context, "Pickup navigation failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        if (showDiffBranchDialog) {
            DiffBranchDialog(
                message = stringResource(R.string.current_branches_not_equal),
                onDismiss = { showDiffBranchDialog = false },
                onChangeBranch = {
                    showDiffBranchDialog = false
                    uiState.targetLatLng?.let {
                        val bundle = Bundle().apply {
                            putString("selectedLocation", uiState.detectedAddress)
                            putString("lat", it.latitude.toString())
                            putString("lng", it.longitude.toString())
                        }
                        val route = "home_screen?lat=${it.latitude}&lng=${it.longitude}&pickup="
                        Log.d("MapScreen", "Navigating to Home from DiffBranchDialog: $route")
                        try {
                            navController.navigate(route) {
                                popUpTo(Screen.MapScreen.route) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            Log.e("MapScreen", "DiffBranch navigation failed: ${e.message}", e)
                            Toast.makeText(context, "DiffBranch navigation failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NoDeliveryDialog(
    message: String,
    onDismiss: () -> Unit,
    onPickupClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.no_delivery_available), fontWeight = FontWeight.Bold, fontSize = 15.sp) },

        confirmButton = {
            TextButton(modifier = Modifier.fillMaxWidth(),onClick = onDismiss) {
                Text(stringResource(R.string.confirm),modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp,color = Color(0xFFFCB203))
            }
        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Change Location", color = Color(0xFFF15A25))
//            }
//        },
        containerColor = Color.White,
       /* modifier = Modifier
            .width(350.dp)
            .height(420.dp)*/
    )
}

@Composable
fun DiffBranchDialog(
    message: String,
    onDismiss: () -> Unit,
    onChangeBranch: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Different Branch", fontWeight = FontWeight.Bold) },
        text = { Text(message, fontSize = 14.sp) },
        confirmButton = {
            TextButton(onClick = onChangeBranch) {
                Text("Change Branch", color = Color(0xFFF15A25))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFFF15A25))
            }
        },
        containerColor = Color.White,
        modifier = Modifier
            .width(350.dp)
            .height(420.dp)
    )
}