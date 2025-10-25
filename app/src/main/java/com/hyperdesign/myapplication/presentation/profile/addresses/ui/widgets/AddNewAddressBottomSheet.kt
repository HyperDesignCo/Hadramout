package com.hyperdesign.myapplication.presentation.profile.addresses.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AreaAndRegionEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomTextField
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesIntents
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesModelState
import com.hyperdesign.myapplication.presentation.profile.addresses.mvi.AddressesViewModel
import com.hyperdesign.myapplication.presentation.profile.addresses.ui.widgets.CustomSearchableDropdownMenu

@SuppressLint("RememberInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAddressBottomSheet(
    viewModel: AddressesViewModel,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    lat: String,
    long: String,
    onBackClick: () -> Unit,
) {
    val state by viewModel.addressState.collectAsStateWithLifecycle()

    val districtFocusRequester = FocusRequester()
    val streetFocusRequester = FocusRequester()
    val phoneFocusRequester = FocusRequester()
    val secondPhoneFocusRequester = FocusRequester()
    val buildingNumberFocusRequester = FocusRequester()
    val floorNumberFocusRequester = FocusRequester()
    val apartmentNumberFocusRequester = FocusRequester()
    val specialSignFocusRequester = FocusRequester()
    val extraInfoFocusRequester = FocusRequester()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
//
//                    item {
//                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
//                        Text(stringResource(R.string.area), fontSize = 17.sp, color = Color.Black)
//                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
//                        CustomSearchableDropdownMenu(
//                            items = state.area?.areas.orEmpty(),
//                            onItemSelected = { /* Not needed */ },
//                            onItemSelectId = { selected ->
//                                viewModel.handleIntents(AddressesIntents.ChangeAreaId(selected.id))
//                            },
//                            selectedItem = state.areaIdValue,
//                            isError = state.areaError != null,
//                            errorMessage = state.areaError,
//                            selectedId = state.areaId.toIntOrNull(),
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }

//                    item {
//                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
//                        Text(stringResource(R.string.district), fontSize = 17.sp, color = Color.Black)
//                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
//                        CustomTextField(
//                            value = state.district,
//                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeDistrictValue(it)) },
//                            borderWidth = 1f,
//                            isError = state.districtError != null,
//                            errorMessage = state.districtError,
//                            modifier = Modifier.fillMaxWidth(),
//                            focusRequester = districtFocusRequester,
//                            nextFocusRequester = streetFocusRequester
//                        )
//                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.street), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.street,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeStreetValue(it)) },
                            borderWidth = 1f,
                            isError = state.streetError != null,
                            errorMessage = state.streetError,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = streetFocusRequester,
                            nextFocusRequester = phoneFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.phone_number), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.phone,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangePhone1Value(it)) },
                            borderWidth = 1f,
                            isError = state.phoneError != null,
                            errorMessage = state.phoneError,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = phoneFocusRequester,
                            nextFocusRequester = secondPhoneFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.second_phone_number), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.secondPhoneNum,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangePhone2Value(it)) },
                            borderWidth = 1f,
                            keyboardType = KeyboardType.Number,
                            isError = state.secondPhoneNumError!=null,
                            errorMessage = state.secondPhoneNumError,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = secondPhoneFocusRequester,
                            nextFocusRequester = buildingNumberFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.building_number), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.buildingNumber,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeBuildingNumValue(it)) },
                            borderWidth = 1f,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = buildingNumberFocusRequester,
                            nextFocusRequester = floorNumberFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.floor_number), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.floorNumber,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeFloorNumValue(it)) },
                            borderWidth = 1f,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = floorNumberFocusRequester,
                            nextFocusRequester = apartmentNumberFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.apartment_number), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.apartmentNumber,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeApartmentNumValue(it)) },
                            borderWidth = 1f,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = apartmentNumberFocusRequester,
                            nextFocusRequester = specialSignFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.special_sign), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.specialSign,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeSpecialSignValue(it)) },
                            borderWidth = 1f,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = specialSignFocusRequester,
                            nextFocusRequester = extraInfoFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        Text(stringResource(R.string.extra_info), fontSize = 17.sp, color = Color.Black)
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        CustomTextField(
                            value = state.extraInfo,
                            onValueChange = { viewModel.handleIntents(AddressesIntents.ChangeExtraInfoValue(it)) },
                            borderWidth = 1f,
                            modifier = Modifier.fillMaxWidth(),
                            focusRequester = extraInfoFocusRequester
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                        CustomButton(
                            text = stringResource(R.string.save),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.handleIntents(
                                    AddressesIntents.SaveNewAddress(
                                        regionId = state.regionId.toString(),
                                        areaId = state.areaId,
                                        district = state.district,
                                        street = state.street,
                                        firstPhoneNum = state.phone,
                                        secondPhoneNum = state.secondPhoneNum,
                                        buildingNum = state.buildingNumber,
                                        floorNum = state.floorNumber,
                                        apartmentNum = state.apartmentNumber,
                                        specialSign = state.specialSign,
                                        extraInfo = state.extraInfo,
                                        lat = lat,
                                        long = long
                                    )
                                )
                                if (state.phone.isNotEmpty()&&state.street.isNotEmpty()&&state.secondPhoneNum.isNotEmpty()&&state.secondPhoneNumError==null&&state.phoneError==null&&state.streetError==null){
                                    onDismissRequest()
                                    onBackClick()
                                }

                            }
                        )
                    }
                }
            }
        }
    }
}