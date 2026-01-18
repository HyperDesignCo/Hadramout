package com.hyperdesign.myapplication.presentation.common.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.viewmodel.MapViewModel
import com.hyperdesign.myapplication.presentation.common.wedgits.distanceMetersTo
import com.hyperdesign.myapplication.presentation.home.HomeObject
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navigateFrom: String = "",
    addressId: String = "",
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

    Log.d("MapScreen", "Received navigateFrom: $navigateFrom, addressId: $addressId")

    val scope = rememberCoroutineScope()
    val uiState by mapViewModel.uiState.collectAsState()

    val defaultZoom = 15f
    val cameraPositionState = rememberCameraPositionState()

    var isMapReady by remember { mutableStateOf(false) }
    var isInitialLocationSet by remember { mutableStateOf(false) }
    var isLoadingLocation by remember { mutableStateOf(true) }

    var showNoDeliveryDialog by remember { mutableStateOf(false) }
    var showDiffBranchDialog by remember { mutableStateOf(false) }


    fun isInvalidLocation(latLng: LatLng?): Boolean {
        return latLng == null || (latLng.latitude == 0.0 && latLng.longitude == 0.0)
    }


    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
            return@LaunchedEffect
        }

        val isFirstLaunch = mapViewModel.isFirstLaunch()

        if (isFirstLaunch) {
            // First launch: Always get REAL current location
            Log.d("MapScreen", "First launch detected - requesting real current location")
            mapViewModel.requestCurrentLocation(context) { currentLatLng ->
                if (currentLatLng != null && !isInvalidLocation(currentLatLng)) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, defaultZoom)
                    mapViewModel.updateTargetLocation(currentLatLng)
                    mapViewModel.setFirstLaunchComplete() // Mark first launch as done
                    isInitialLocationSet = true
                    Log.d("MapScreen", "First launch: Got REAL current location → $currentLatLng")
                } else {
                    Log.w("MapScreen", "Failed to get current location on first launch")
                    mapViewModel.setFirstLaunchComplete()
                }
                isLoadingLocation = false
            }
        } else {
            // Subsequent launches: Use saved location
            val savedLocation = mapViewModel.getSavedLocation()
            if (savedLocation != null && !isInvalidLocation(savedLocation)) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(savedLocation, defaultZoom)
                mapViewModel.updateTargetLocation(savedLocation)
                isInitialLocationSet = true
                isLoadingLocation = false
                Log.d("MapScreen", "Subsequent launch: Loaded saved location: $savedLocation")
            } else {
                // Fallback: If saved location is invalid, request current location
                Log.w("MapScreen", "Saved location is invalid, requesting current location")
                mapViewModel.requestCurrentLocation(context) { currentLatLng ->
                    if (currentLatLng != null && !isInvalidLocation(currentLatLng)) {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLatLng, defaultZoom)
                        mapViewModel.updateTargetLocation(currentLatLng)
                        isInitialLocationSet = true
                        Log.d("MapScreen", "Got current location from fallback → $currentLatLng")
                    }
                    isLoadingLocation = false
                }
            }
        }
    }

    LaunchedEffect(uiState.targetLatLng, isMapReady) {
        if (!isMapReady || isLoadingLocation) return@LaunchedEffect

        uiState.targetLatLng?.let { target ->
            if (isInvalidLocation(target)) return@let

            val current = cameraPositionState.position.target
            val distance = current.distanceMetersTo(target)

            if (distance > 100 || !isInitialLocationSet) {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(target, defaultZoom)
                )
                isInitialLocationSet = true
            }
        }
    }


    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving && isMapReady && !isLoadingLocation) {
            delay(400)
            val newTarget = cameraPositionState.position.target
            if (!isInvalidLocation(newTarget)) {
                mapViewModel.updateTargetLocation(newTarget)
                Log.d("MapScreen", "Camera idle, updated location: $newTarget")
            }
        }
    }


    when {
        !permissionState.allPermissionsGranted -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.location_permission_required))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { permissionState.launchMultiplePermissionRequest() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15A25))
                    ) {
                        Text(stringResource(R.string.grant_permission), color = Color.White)
                    }
                }
            }
        }

        isLoadingLocation -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    CircularProgressIndicator(color = Color(0xFFF15A25))
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = "جاري تحديد موقعك الحالي...",
//                        color = Color.Gray,
//                        fontSize = 16.sp
//                    )
//                }
//            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = true),
                    onMapLoaded = {
                        isMapReady = true
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
                        Column(modifier = Modifier.padding(12.dp)) {
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
                                ) { deliveryStatus, currentRestaurantBranch, areaId ->
                                    Log.d("MapScreen", "checkLocationDelivery result: deliveryStatus=$deliveryStatus, currentRestaurantBranch=$currentRestaurantBranch")

                                    when (deliveryStatus) {
                                        "1" -> {
                                            if (currentRestaurantBranch == null ||
                                                mapViewModel.tokenManger.getCurrentResturentBranch() == currentRestaurantBranch
                                            ) {
                                                mapViewModel.saveLocation(latLng)
                                                val bundle = Bundle().apply {
                                                    putString("selectedLocation", address)
                                                    putString("lat", latLng.latitude.toString())
                                                    putString("lng", latLng.longitude.toString())
                                                }

                                                when (navigateFrom) {
                                                    "Home" -> {
                                                        val route = "home_screen?lat=${latLng.latitude}&lng=${latLng.longitude}&pickup="
                                                        try {
                                                            navController.navigate(route) {
                                                                popUpTo(Screen.HomeScreen.route) { inclusive = true }
                                                            }
                                                        } catch (e: Exception) {
                                                            Log.e("MapScreen", "Navigation failed", e)
                                                            Toast.makeText(context, "Navigation failed", Toast.LENGTH_LONG).show()
                                                        }
                                                    }

                                                    "checkOutScreen" -> {
                                                        val route = "${Screen.AllAddressesScreen.route.replace("{screenType}","checkOutScreen")}?lat=${latLng.latitude}&lng=${latLng.longitude}&areaId=${areaId}"
                                                        try {
                                                            navController.navigate(route) {
                                                                popUpTo(Screen.AllAddressesScreen.route) { inclusive = true }
                                                            }
                                                        } catch (e: Exception) {
                                                            Log.e("MapScreen", "Navigation failed", e)
                                                            Toast.makeText(context, "Navigation failed", Toast.LENGTH_LONG).show()
                                                        }
                                                    }

                                                    "addAddress" -> {
                                                        val route = "all_address_screen/{screenType}?lat=${latLng.latitude}&lng=${latLng.longitude}&areaId=${areaId}"
                                                        try {
                                                            navController.navigate(route) {
                                                                popUpTo(Screen.AllAddressesScreen.route) { inclusive = true }
                                                            }
                                                        } catch (e: Exception) {
                                                            Log.e("MapScreen", "Navigation failed", e)
                                                            Toast.makeText(context, "Navigation failed", Toast.LENGTH_LONG).show()
                                                        }
                                                    }

                                                    "editAddress" -> {
                                                        val route = "update_address_screen/{addressId}?lat=${latLng.latitude}&lng=${latLng.longitude}&areaId=${areaId}"
                                                        try {
                                                            navController.navigate(route) {
                                                                popUpTo(Screen.UpdateAddressScreen.route) { inclusive = true }
                                                            }
                                                        } catch (e: Exception) {
                                                            Log.e("MapScreen", "Navigation failed", e)
                                                            Toast.makeText(context, "Navigation failed", Toast.LENGTH_LONG).show()
                                                        }
                                                    }

                                                    else -> {
                                                        Toast.makeText(context, "Navigation not implemented for $navigateFrom", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } else {
                                                showDiffBranchDialog = true
                                            }
                                        }

                                        "0" -> showNoDeliveryDialog = true

                                        else -> showNoDeliveryDialog = true
                                    }
                                }
                            }
                        } ?: run {
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

                if (uiState.error != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = uiState.error ?: "حدث خطأ غير معروف",
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(
                                onClick = { mapViewModel.requestCurrentLocation(context) {} },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF15A25))
                            ) {
                                Text("إعادة المحاولة", color = Color.White)
                            }
                        }
                    }
                }

                if (showNoDeliveryDialog) {
                    NoDeliveryDialog(
                        onDismiss = { showNoDeliveryDialog = false },
                        onPickupClick = {
                            showNoDeliveryDialog = false
                            val route = "home_screen?pickup=1"
                            try {
                                navController.navigate(route) {
                                    popUpTo(Screen.MapScreen.route) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                Log.e("MapScreen", "Pickup navigation failed", e)
                                Toast.makeText(context, "فشل الانتقال", Toast.LENGTH_LONG).show()
                            }
                            HomeObject.updateStatus(0)
                        }
                    )
                }

                if (showDiffBranchDialog) {
                    DiffBranchDialog(
                        message = stringResource(R.string.current_branches_not_equal),
                        onDismiss = { showDiffBranchDialog = false },
                        onChangeBranch = {
                            showDiffBranchDialog = false
                            uiState.targetLatLng?.let { latLng ->
                                val route = "home_screen?lat=${latLng.latitude}&lng=${latLng.longitude}&pickup="
                                try {
                                    navController.navigate(route) {
                                        popUpTo(Screen.MapScreen.route) { inclusive = true }
                                    }
                                } catch (e: Exception) {
                                    Log.e("MapScreen", "DiffBranch navigation failed", e)
                                    Toast.makeText(context, "فشل تغيير الفرع", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NoDeliveryDialog(
    onDismiss: () -> Unit,
    onPickupClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                stringResource(R.string.no_delivery_available),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text(
                    stringResource(R.string.change_Location),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFFCB203)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onPickupClick) {
                Text(
                    stringResource(R.string.pick_up),
                    color = Color(0xFFF15A25),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        },
        containerColor = Color.White
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
        title = {
            Text(
                text = stringResource(R.string.different_branch),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = { /* empty as in original */ },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onChangeBranch) {
                    Text(
                        text = stringResource(R.string.yes),
                        color = Color(0xFFF15A25),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.no),
                        color = Color(0xFFF15A25),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {},
        containerColor = Color.White
    )
}