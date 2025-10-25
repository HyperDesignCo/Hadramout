package com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AreaAndRegionEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesIntents
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.UpdateAddressIntents
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.UpdateAddressViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.widgets.CustomSearchableDropdownMenu
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RememberInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAddressScreen(addressId: String,navBackStackEntry: NavBackStackEntry?=null, viewModel: UpdateAddressViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val addressState by viewModel.addressState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    val myAddressId = viewModel.tokenManager.getAddressId().orEmpty()
    // Fetch address data when screen loads
    LaunchedEffect(myAddressId) {
        Log.d("UpdateAddressScreen", "Fetching address with id: $myAddressId")
        viewModel.handleIntents(UpdateAddressIntents.FetchAddress(myAddressId))
//        viewModel.handleIntents(UpdateAddressIntents.GetRegions) // Ensure regions are fetched
    }


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
                    addressState.lat=lat
                    addressState.long =lng
                    viewModel.handleIntents(UpdateAddressIntents.ChangeAreaId(areaId.toInt()))

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

    LaunchedEffect(addressState) {
        viewModel.addressChannel.collectLatest { event ->
            when (event) {
                is ValidationEvent.Failure -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
                ValidationEvent.Success -> {
                    navController.navigate(Screen.AllAddressesScreen.route){
                        popUpTo(Screen.AllAddressesScreen.route){true}
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            MainHeader(
                onBackPressesd = { navController.popBackStack() },
                showTitle = true,
                title = stringResource(R.string.update_address),
                showBackPressedIcon = true,
                height = 90
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {


//                item {
//                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
//                    Text(stringResource(R.string.area), fontSize = 17.sp, color = Color.Black)
//                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
//                    CustomSearchableDropdownMenu(
//                        items = addressState.area?.areas.orEmpty(),
//                        onItemSelected = { /* Not needed */ },
//                        onItemSelectId = { selected ->
//                            viewModel.handleIntents(UpdateAddressIntents.ChangeAreaId(selected.id))
//                        },
//                        selectedItem = addressState.areaId,
//                        isError = addressState.areaError != null,
//                        errorMessage = addressState.areaError,
//                        selectedId = addressState.areaId.toIntOrNull(),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//
//                item {
//                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
//                    Text(stringResource(R.string.district), fontSize = 17.sp, color = Color.Black)
//                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
//                    CustomTextField(
//                        value = addressState.district,
//                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeDistrictValue(it)) },
//                        borderWidth = 1f,
//                        isError = false,
//                        errorMessage = null,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.street), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.street,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeStreetValue(it)) },
                        borderWidth = 1f,
                        isError = false,
                        errorMessage = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.phone_number), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.phone,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangePhone1Value(it)) },
                        borderWidth = 1f,
                        isError = false,
                        errorMessage = null,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.second_phone_number), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.secondPhoneNum,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangePhone2Value(it)) },
                        borderWidth = 1f,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.building_number), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.buildingNumber,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeBuildingNumValue(it)) },
                        borderWidth = 1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.floor_number), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.floorNumber,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeFloorNumValue(it)) },
                        borderWidth = 1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.apartment_number), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.apartmentNumber,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeApartmentNumValue(it)) },
                        borderWidth = 1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.special_sign), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.specialSign,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeSpecialSignValue(it)) },
                        borderWidth = 1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    Text(stringResource(R.string.extra_info), fontSize = 17.sp, color = Color.Black)
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    CustomTextField(
                        value = addressState.extraInfo,
                        onValueChange = { viewModel.handleIntents(UpdateAddressIntents.ChangeExtraInfoValue(it)) },
                        borderWidth = 1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    CustomButton(
                        text = stringResource(R.string.update),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.handleIntents(
                                UpdateAddressIntents.SaveUpdatedAddress(
                                    areaId = addressState.areaId,
                                    street = addressState.street,
                                    firstPhoneNum = addressState.phone,
                                    secondPhoneNum = addressState.secondPhoneNum,
                                    buildingNum = addressState.buildingNumber,
                                    floorNum = addressState.floorNumber,
                                    apartmentNum = addressState.apartmentNumber,
                                    specialSign = addressState.specialSign,
                                    extraInfo = addressState.extraInfo,
                                    lat = addressState.lat,
                                    long = addressState.long
                                )
                            )
                        }
                    )
                }
            }
        }

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
    }
}