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
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
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
    val scope = rememberCoroutineScope()
    val uiState by mapViewModel.uiState.collectAsState()
    val defaultZoom = 15f
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), defaultZoom)
    }
    val density = LocalDensity.current
    var showNoDeliveryDialog by remember { mutableStateOf(false) }
    var showDiffBranchDialog by remember { mutableStateOf(false) }
    var isInitialLocationSet by remember { mutableStateOf(false) }

    // Request location permissions and load initial location
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        } else {
            mapViewModel.requestCurrentLocation(context)
        }
    }

    // Move camera to initial location with default zoom
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

    // Update location when camera stops moving
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            delay(300) // Debounce to avoid excessive updates
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
//                cameraPositionState.animate(
//                    CameraUpdateFactory.newLatLngZoom(latLng, cameraPositionState.position.zoom)
//                )
                Log.d("MapScreen", "Map clicked, updated location: $latLng")
            }
        )

        // Fixed marker image at the center
        Image(
            painter = painterResource(R.drawable.mark),
            contentDescription = "Fixed Marker",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-16).dp) // TranslationY as in XML
        )

        // Search bar and predictions
//        Column(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
//                .background(Color.White, RoundedCornerShape(25.dp))
//                .padding(horizontal = 12.dp, vertical = 8.dp)
//                .width(300.dp)
//        ) {
//            var query by remember { mutableStateOf("") }
//            TextField(
//                value = query,
//                onValueChange = {
//                    query = it
//                    mapViewModel.searchPlaces(it)
//                },
//                placeholder = { Text(stringResource(R.string.search_for_an_location)) },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                ),
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = ""
//                    )
//                }
//            )
//            if (uiState.predictions.isNotEmpty()) {
//                LazyColumn(
//                    modifier = Modifier
//                        .heightIn(max = 200.dp)
//                        .background(Color.White)
//                        .fillMaxWidth()
//                ) {
//                    items(uiState.predictions) { prediction ->
//                        ListItem(
//                            headlineContent = { Text(prediction.getPrimaryText(null).toString()) },
//                            supportingContent = { Text(prediction.getSecondaryText(null).toString()) },
//                            modifier = Modifier
//                                .clickable {
//                                    mapViewModel.selectPlace(prediction.placeId)
//                                    query = prediction.getPrimaryText(null).toString()
//                                }
//                                .fillMaxWidth()
//                        )
//                    }
//                }
//            }
//        }

        // Detected location card
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

        // Confirm button
        Button(
            onClick = {
                uiState.targetLatLng?.let { latLng ->
                    scope.launch {
                        val address = mapViewModel.reverseGeocode(latLng)
                        mapViewModel.checkLocationDelivery(
                            context,
                            latLng.latitude.toString(),
                            latLng.longitude.toString()
                        ) { deliveryStatus, currentRestaurantBranch ->
                            // Your commented navigation logic
                            // val currentRerBranch = SharedPrefManager.getDefaults("userResBranch", context)
                            // when (deliveryStatus) {
                            //     "1" -> {
                            //         if (currentRerBranch == null || currentRerBranch == currentRestaurantBranch) {
                            //             val bundle = Bundle().apply {
                            //                 putString("selectedLocation", address)
                            //                 putString("lat", latLng.latitude.toString())
                            //                 putString("lng", latLng.longitude.toString())
                            //             }
                            //             when (navigateFrom) {
                            //                 "Add", "Cart" -> navController.navigate("addNewAddressFragment", bundle)
                            //                 "Edit" -> navController.navigate("editSaveAddressFragment", bundle)
                            //                 "Home", "Menu", "HomeToMap" -> {
                            //                     if (navigateFrom == "HomeToMap") {
                            //                         navController.navigate("homeFragment", bundle)
                            //                     } else {
                            //                         navController.popBackStack()
                            //                     }
                            //                 }
                            //             }
                            //         } else {
                            //             showDiffBranchDialog = true
                            //         }
                            //     }
                            //     "0" -> showNoDeliveryDialog = true
                            //     else -> showNoDeliveryDialog = true
                            // }
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

        // Dialogs
        if (showNoDeliveryDialog) {
            // NoDeliveryDialog(
            //     message = context.getString(R.string.change_location_not_work),
            //     onDismiss = { showNoDeliveryDialog = false },
            //     onPickupClick = {
            //         Variable.setChangePickupFromMap("1")
            //         showNoDeliveryDialog = false
            //         navController.navigate("homeFragment")
            //     }
            // )
        }

        if (showDiffBranchDialog) {
            // DiffBranchDialog(
            //     message = context.getString(R.string.current_branches_not_equal),
            //     onDismiss = { showDiffBranchDialog = false },
            //     onChangeBranch = {
            //         showDiffBranchDialog = false
            //         val bundle = uiState.targetLatLng?.let {
            //             Bundle().apply {
            //                 putString("selectedLocation", uiState.detectedAddress)
            //                 putString("lat", it.latitude.toString())
            //                 putString("lng", it.longitude.toString())
            //             }
            //         }
            //         navController.navigate("homeFragment", bundle)
            //     }
            // )
        }
    }
}
