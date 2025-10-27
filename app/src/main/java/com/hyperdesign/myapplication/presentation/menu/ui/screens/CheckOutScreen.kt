package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.BranchInfoEntity
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutIntents
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutStateModel
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutViewModel
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.CartBottomBar
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.CheckOutHeader
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.PaymentsOption
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.ShowAddress
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SpecialRequestEditText
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.showBranchDetails
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckOutScreen(
    deliveryTime:String?,
    checkOutViewModel: CheckOutViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val checkState by checkOutViewModel.checkOutState.collectAsStateWithLifecycle()
    var subRegion by remember { mutableStateOf("") }
    var allAddress by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf("") }
    val context = LocalContext.current
    var finishOrderMsg by remember { mutableStateOf("") }
    var paymentId by remember { mutableStateOf("") }
    var addressList by remember { mutableStateOf<List<Pair<String, String>>?>(null) }
    var justAdded by remember { mutableStateOf(false) }
    var deliveryStatus by remember { mutableStateOf(false) }
    Log.d("lang",loginViewModel.tokenManager.getLanguage().toString())




    // Listen for address_added flag from AllAddressesScreen
    val addressAdded by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Boolean>("address_added", false)
        ?.collectAsStateWithLifecycle(initialValue = false) ?: remember { mutableStateOf(false) }

    // Refresh data when address_added is true
    LaunchedEffect(addressAdded) {
        if (addressAdded) {
            Log.d("CheckOutScreen", "address_added detected, refreshing data")
            checkOutViewModel.handleIntents(CheckOutIntents.CheckOutClick("2"))
            checkOutViewModel.handleIntents(CheckOutIntents.GetAddress)
            delay(1000) // Add delay to account for potential backend synchronization delay
            justAdded = true
            navController.currentBackStackEntry?.savedStateHandle?.set("address_added", false)
        }
    }

    // Initial data fetch
    LaunchedEffect(Unit) {
        Log.d("CheckOutScreen", "Initial data fetch")
        checkOutViewModel.handleIntents(CheckOutIntents.CheckOutClick("2"))
    }

    // Update UI when checkState changes
    LaunchedEffect(checkState) {
        Log.d("CheckOutScreen", "checkState updated, addresses: ${checkState.address?.addresses?.size}")
        // Select the most recently added address (last in the list)
        val address = checkState.address?.addresses?.firstOrNull()
        subRegion = listOfNotNull(
            address?.area?.name?.takeIf { it.isNotEmpty() },
            address?.region?.name?.takeIf { it.isNotEmpty() }
        ).joinToString(separator = ",")
        allAddress = listOfNotNull(
            address?.sub_region?.takeIf { it.isNotEmpty() },
            address?.street?.takeIf { it.isNotEmpty() },
            address?.special_sign?.takeIf { it.isNotEmpty() },
            address?.building_number?.takeIf { it.isNotEmpty() },
            address?.floor_number?.takeIf { it.isNotEmpty() }
        ).joinToString(separator = ",")
        paymentId = checkState.checkOutResponse?.payment_methods?.firstOrNull()?.id.toString()
        selectedPayment = checkState.checkOutResponse?.payment_methods?.firstOrNull()?.title.toString()
        finishOrderMsg = checkState.finishOrderResponse?.message.orEmpty()

        addressList = checkState.address?.addresses?.map { addr ->
            val district = listOfNotNull(
                addr.area?.name?.takeIf { it.isNotEmpty() },
                addr.region?.name?.takeIf { it.isNotEmpty() }
            ).joinToString(separator = ",")
            val addressDetails = listOfNotNull(
                addr.sub_region?.takeIf { it.isNotEmpty() },
                addr.street?.takeIf { it.isNotEmpty() },
                addr.special_sign?.takeIf { it.isNotEmpty() },
                addr.building_number?.takeIf { it.isNotEmpty() },
                addr.floor_number?.takeIf { it.isNotEmpty() }
            ).joinToString(separator = ",")
            Pair(district, addressDetails)
        } ?: emptyList()

        // Auto-select the latest address if a new one was added
        if (addressList?.isNotEmpty() == true && justAdded) {
            Log.d("CheckOutScreen", "Auto-selecting latest address: ${addressList!!.last()}")
            subRegion = addressList!!.last().first
            allAddress = addressList!!.last().second
            justAdded = false
        }
        deliveryStatus=if(checkState.checkOutResponse?.cart?.pickUpStatus=="0") true else false

    }

    // Handle order completion messages
    LaunchedEffect(finishOrderMsg) {
        if (finishOrderMsg.isNotEmpty()) {
            Log.d("CheckOutScreen", "Finish order message: $finishOrderMsg")
            if (finishOrderMsg == "Your order has been sent successfully") {
                Toast.makeText(context, context.getString(R.string.your_order_has_been_sent_successfully), Toast.LENGTH_SHORT).show()
                navController.navigate("${Screen.HomeScreen.route}?lat=noDialoge") {
                    popUpTo(Screen.HomeScreen.route) { inclusive = true }
                    launchSingleTop=true
                }
            } else {
                Toast.makeText(context, finishOrderMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        addressList?.let { addresses ->
            CheckOutScreenContent(
                name = loginViewModel.tokenManager.getUserData()?.name.orEmpty(),
                phoneNumber = loginViewModel.tokenManager.getUserData()?.mobile.orEmpty(),
                image = loginViewModel.tokenManager.getUserData()?.image.orEmpty(),
                state = checkState,
                onChangeSpecialRequest = {
                    checkOutViewModel.handleIntents(CheckOutIntents.OnChangeSpecialRequest(it))
                },
                onBackedBresed = {
                    navController.popBackStack()
                },
                subRegion = subRegion,
                allAddress = allAddress,
                onSelectedPayemt = { payment -> selectedPayment = payment },
                selectedPayemnt = selectedPayment,
                onChangePaymentId = { paymentIdd -> paymentId = paymentIdd },
                finishOrder = { cartId ->
                    checkOutViewModel.handleIntents(
                        CheckOutIntents.FinishOrder(
                            cartId = cartId,
                            paymentMethodId = paymentId,
                            specialRequest = checkState.specialRequest,
                            userId = loginViewModel.tokenManager.getUserData()?.id.toString()
                        )
                    )
                },
                addressList = addresses,
                onAddressSelected = { district, details ->
                    Log.d("CheckOutScreen", "Address selected: $district, $details")
                    subRegion = district
                    allAddress = details
                },
                onClickToAddNewAddress = {
                    Log.d("CheckOutScreen", "Navigating to AllAddressesScreen")
                    val screenType = "checkOutScreen"
                    navController.navigate(Screen.AllAddressesScreen.route.replace("{screenType}", screenType))
                },
                pickUpStatus = deliveryStatus,
                branchName = if(loginViewModel.tokenManager.getLanguage()=="en") checkState.checkOutResponse?.branch?.titleEn else checkState.checkOutResponse?.branch?.titleAr,
                branchAddress =if(loginViewModel.tokenManager.getLanguage()=="en") checkState.checkOutResponse?.branch?.addressEn else checkState.checkOutResponse?.branch?.addressAr,
                deliveryTime = deliveryTime?:""

            )
        }
        if (checkState.isLoading) {
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

@Composable
fun CheckOutScreenContent(
    addressList: List<Pair<String, String>>,
    onAddressSelected: (String, String) -> Unit,
    finishOrder: (String) -> Unit,
    onChangePaymentId: (String) -> Unit,
    selectedPayemnt: String,
    onSelectedPayemt: (String) -> Unit,
    subRegion: String,
    allAddress: String,
    state: CheckOutStateModel,
    onChangeSpecialRequest: (String) -> Unit,
    name: String,
    phoneNumber: String,
    image: String? = null,
    onBackedBresed: () -> Unit,
    onClickToAddNewAddress: () -> Unit,
    pickUpStatus : Boolean,
    branchName: String?,
    branchAddress:String?,
    deliveryTime:String


) {
    Log.d("pickUpStatus",pickUpStatus.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CheckOutHeader(
            userName = name,
            phoneNumber = phoneNumber,
            image = image,
            onBackPressed = onBackedBresed
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SpecialRequestEditText(state = state, onChangeSpecialRequest = onChangeSpecialRequest)
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                if(pickUpStatus){
                    if (allAddress.isNotEmpty() && subRegion.isNotEmpty()) {
                        ShowAddress(
                            district = subRegion,
                            addressDetails = allAddress,
                            addressList = addressList,
                            onAddressSelected = onAddressSelected
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 15.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.add_new_address),
                            onClick = onClickToAddNewAddress
                        )
                    } else {
                        CustomButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 15.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.add_new_address),
                            onClick = onClickToAddNewAddress
                        )
                    }
                }else{
                    showBranchDetails(
                        branchName =branchName,
                        branchaddress = branchAddress
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(Gray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = stringResource(R.string.payment_type),
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = stringResource(R.string.pay_with_card_or_cash),
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Gray,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        state.checkOutResponse?.payment_methods?.forEach { payment ->
                            PaymentsOption(
                                payment = payment.title,
                                selectedPayment = selectedPayemnt,
                                onSelected = {
                                    onSelectedPayemt(it)
                                    onChangePaymentId(payment.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        CartBottomBar(
            priceItems = state.checkOutResponse?.cart?.primaryPrice?.toString() ?: "0.00",
            deliveryPrice = state.checkOutResponse?.cart?.deliveryCost?.toString() ?: "0.00",
            totalPrice = state.checkOutResponse?.cart?.totalPrice?.toString() ?: "0.00",
            buttonText = stringResource(R.string.checkout),
            onPayClick = {
                finishOrder(state.checkOutResponse?.cart?.id.orEmpty())
            },
            pickUpStatus = pickUpStatus,
            deliveryTime = deliveryTime,

            )
    }
}