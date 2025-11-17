package com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.google.android.gms.maps.model.LatLng
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AddressEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.home.mvi.HomeIntents
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesIntents
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.UpdateAddressViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.widgets.AddressItem
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAddressesScreen(type:String?,navBackStackEntry: NavBackStackEntry?=null,addressViewModel: AddressesViewModel = koinViewModel(),updateAddressViewModel: UpdateAddressViewModel=koinViewModel()) {
    val navController = LocalNavController.current
    val addressState by addressViewModel.addressState.collectAsStateWithLifecycle()
    var addresses by remember { mutableStateOf<List<AddressEntity>>(emptyList()) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var showRationaleDialog by remember { mutableStateOf(false) }

//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            addressViewModel.fetchCurrentLocation()
//        } else {
//            Log.w("Permission", "Location permission denied")
//        }
//    }


    val navArgsKey by rememberUpdatedState(
        "${navBackStackEntry?.arguments?.getString("lat")}:${navBackStackEntry?.arguments?.getString("lng")}:${navBackStackEntry?.arguments?.getString("pickup")}"
    )


    LaunchedEffect(navArgsKey) {
        navBackStackEntry?.arguments?.let { args ->
            val lat = args.getString("lat")
            val lng = args.getString("lng")
            val areaId = args.getString("areaId")
            Log.d("Alladdress", "Received args from MapScreen: lat=$lat, lng=$lng, areaId=$areaId")

            if (lat != null && lng != null&& areaId!=null && lat.isNotEmpty() && lng.isNotEmpty()&& areaId.isNotEmpty()) {
                try {
                    showBottomSheet=true
                    addressState.lat=lat
                    addressState.long =lng
                    addressViewModel.handleIntents(AddressesIntents.ChangeAreaId(areaId.toInt()))

//                    userLocation = LatLng(lat.toDouble(), lng.toDouble())
//                    homeViewModel.handleIntents(HomeIntents.ChangeLat(lat))
//                    homeViewModel.handleIntents(HomeIntents.ChangeLng(lng))
//                    homeViewModel.handleIntents(HomeIntents.CheckLocation)
//                    Log.d("HomeScreen", "Processed location from MapScreen: $userLocation")
                } catch (e: NumberFormatException) {
                    Log.e("HomeScreen", "Invalid lat/lng format: lat=$lat, lng=$lng", e)
                }
            }
        }
    }

    // Listen to addressState for updates and channel events
    LaunchedEffect(addressState) {
        addresses = addressState.addresses?.addresses.orEmpty()
        Log.d("AllAddressesScreen", "AddressState updated: deleteAddressResponse=${addressState.deleteAddressRespnse?.message}")

        addressViewModel.addressChannel.collectLatest { event ->
            Log.d("AllAddressesScreen", "Received event: $event")
            when (event) {
                is ValidationEvent.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
                ValidationEvent.Success -> {
                    Log.d("AllAddressesScreen", "Success event triggered, calling GetAddress")
                    addressViewModel.handleIntents(AddressesIntents.GetAddress)
                }
            }
        }
    }

    LaunchedEffect(Unit) {

        addressViewModel.handleIntents(AddressesIntents.GetAddress)
    }

    // Check and request permission when screen is entered


    // Rationale dialog
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Location Permission Required") },
            text = { Text("This app needs location permission to fetch your current address. Please allow it to continue.") },
            confirmButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
//                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    Text(stringResource(R.string.allow))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) {
                    Text(stringResource(R.string.deny))
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AllAddressesScreenContent(
            onGoToUpdateAddressId = { id ->

                val addressId = Uri.encode(id )
                Log.d("myAddressId ",addressId)
                addressViewModel.tokenManager.setAddressId(addressId)
                navController.navigate("${Screen.MapScreen.route.replace("{navigateFrom}", "editAddress")}?addressId=$addressId")
            },
            onDeleteAddress = { id -> addressViewModel.handleIntents(AddressesIntents.DeleteAddress(id)) },
            onBackPressed = { navController.popBackStack() },
            addresses = addresses,
            onAddNewAddressClick = {
                navController.navigate(Screen.MapScreen.route.replace("{navigateFrom}","addAddress"))
//                showBottomSheet = true
            }
        )

        if (addressState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Secondry)
            }
        }

        if (showBottomSheet) {
            AddNewAddressBottomSheet(
                viewModel = addressViewModel,
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                lat = addressState.lat,
                long = addressState.long,
                onBackClick = {
                    if(type=="checkOutScreen"){
                        navController.previousBackStackEntry?.savedStateHandle?.set("address_added", true)
//                        navController.popBackStack()

                        navController.navigate(Screen.CheckOutScreen.route) {
                            popUpTo(Screen.MapScreen.route) {
                                inclusive = true

                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun AllAddressesScreenContent(
    onGoToUpdateAddressId: (String) -> Unit,
    onDeleteAddress: (String) -> Unit,
    onBackPressed: () -> Unit,
    addresses: List<AddressEntity>,
    onAddNewAddressClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            onBackPressesd = { onBackPressed() },
            showTitle = true,
            title = stringResource(R.string.Addresses),
            showBackPressedIcon = true,
            height = 90
        )

        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
            }

            items(addresses, key = { address -> address.id }) { address ->
                AddressItem(
                    address = address,
                    onDeleteAddress = { onDeleteAddress(address.id) },
                    onGoToUpdateAddress = { onGoToUpdateAddressId(address.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }


        CustomButton(
            text = stringResource(R.string.add_new_address),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            onClick = onAddNewAddressClick
        )
    }
}