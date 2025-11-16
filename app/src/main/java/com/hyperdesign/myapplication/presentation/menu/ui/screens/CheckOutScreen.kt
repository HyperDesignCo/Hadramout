package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutIntents
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutStateModel
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutViewModel
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

/* ============================================================= */
/* =====================  CHECK-OUT SCREEN  ==================== */
/* ============================================================= */

@Composable
fun CheckOutScreen(
    deliveryTime: String?,
    dateTimePicker: String?,
    checkOutViewModel: CheckOutViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val checkState by checkOutViewModel.checkOutState.collectAsStateWithLifecycle()

    // ---------- UI state ----------
    var subRegion by remember { mutableStateOf("") }
    var allAddress by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf("") }
    var selectedOrder by remember { mutableStateOf("") }
    var selectedPreOrder by remember { mutableStateOf("") }
    var selectedDeliveryTime by remember { mutableStateOf(dateTimePicker ?: "") }
    var paymentId by remember { mutableStateOf("1") }
    var finishOrderMsg by remember { mutableStateOf("") }
    var addressList by remember { mutableStateOf<List<Pair<String, String>>?>(null) }
    var justAdded by remember { mutableStateOf(false) }
    var deliveryStatus by remember { mutableStateOf(false) }

    // Scroll-driven expand/collapse state
    var isBottomBarExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var showEnterAddressDialoge by remember { mutableStateOf(false) }

    val context = LocalContext.current

    /* -------------------- SCROLL DETECTION -------------------- */
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                if (index > 0 || offset > 100) {
                    isBottomBarExpanded = true
                } else if (index == 0 && offset < 50) {
                    isBottomBarExpanded = false
                }
            }
    }

    /* -------------------- TIME PICKER RETURN -------------------- */
    val returnedDeliveryTime by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("selected_delivery_time", null)
        ?.collectAsStateWithLifecycle(initialValue = null) ?: remember { mutableStateOf(null) }

    LaunchedEffect(returnedDeliveryTime) {
        returnedDeliveryTime?.let {
            selectedDeliveryTime = it
        }
    }

    /* -------------------- ADDRESS ADDED FLAG -------------------- */
    val addressAdded by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Boolean>("address_added", false)
        ?.collectAsStateWithLifecycle(initialValue = false) ?: remember { mutableStateOf(false) }

    LaunchedEffect(addressAdded) {
        if (addressAdded) {
            checkOutViewModel.handleIntents(CheckOutIntents.CheckOutClick(loginViewModel.tokenManager.getBranchId().toString()))
            checkOutViewModel.handleIntents(CheckOutIntents.GetAddress)
            delay(1000)
            justAdded = true
            navController.currentBackStackEntry?.savedStateHandle?.set("address_added", false)
        }
    }

    /* -------------------- INITIAL DATA -------------------- */
    LaunchedEffect(Unit) {
        checkOutViewModel.handleIntents(CheckOutIntents.CheckOutClick(loginViewModel.tokenManager.getBranchId().toString()))
    }

    /* -------------------- STATE UPDATE -------------------- */
    LaunchedEffect(checkState) {
        val address = checkState.address?.addresses?.firstOrNull()
        subRegion = listOfNotNull(
            address?.area?.name?.takeIf { it.isNotEmpty() },
            address?.region?.name?.takeIf { it.isNotEmpty() }
        ).joinToString(separator = ", ")

        allAddress = listOfNotNull(
            address?.sub_region?.takeIf { it.isNotEmpty() },
            address?.street?.takeIf { it.isNotEmpty() },
            address?.special_sign?.takeIf { it.isNotEmpty() },
            address?.building_number?.takeIf { it.isNotEmpty() },
            address?.floor_number?.takeIf { it.isNotEmpty() }
        ).joinToString(separator = ", ")

        paymentId = checkState.checkOutResponse?.payment_methods?.firstOrNull()?.id.toString()
        Log.d("paymentId", paymentId)
        selectedPayment = checkState.checkOutResponse?.payment_methods?.firstOrNull()?.title.orEmpty()
        finishOrderMsg = checkState.finishOrderResponse?.message.orEmpty()

        if (selectedDeliveryTime.isEmpty()) {
            selectedOrder = context.getString(R.string.order_now)
            selectedPreOrder = "0"
        } else {
            selectedOrder = context.getString(R.string.order_later)
            selectedPreOrder = "1"
        }

        addressList = checkState.address?.addresses?.map { addr ->
            val district = listOfNotNull(
                addr.area?.name?.takeIf { it.isNotEmpty() },
                addr.region?.name?.takeIf { it.isNotEmpty() }
            ).joinToString(separator = ", ")

            val details = listOfNotNull(
                addr.sub_region?.takeIf { it.isNotEmpty() },
                addr.street?.takeIf { it.isNotEmpty() },
                addr.special_sign?.takeIf { it.isNotEmpty() },
                addr.building_number?.takeIf { it.isNotEmpty() },
                addr.floor_number?.takeIf { it.isNotEmpty() }
            ).joinToString(separator = ", ")

            Pair(district, details)
        } ?: emptyList()

        if (addressList?.isNotEmpty() == true && justAdded) {
            val last = addressList!!.last()
            subRegion = last.first
            allAddress = last.second
            justAdded = false
        }

        deliveryStatus = checkState.checkOutResponse?.cart?.pickUpStatus == "0"
    }

    /* -------------------- FINISH ORDER MESSAGE -------------------- */
    LaunchedEffect(finishOrderMsg) {
        if (finishOrderMsg.isNotEmpty()) {
            if (finishOrderMsg == "Your order has been sent successfully") {
                Toast.makeText(context, context.getString(R.string.your_order_has_been_sent_successfully), Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.MyOrders.route) {
                    popUpTo(Screen.CartScreen.route) { inclusive = true }
                }
            } else {
                Toast.makeText(context, finishOrderMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* -------------------- UI -------------------- */
    Box(modifier = Modifier.fillMaxSize()) {
        addressList?.let { addresses ->
            CheckOutScreenContent(
                name = loginViewModel.tokenManager.getUserData()?.name.orEmpty(),
                phoneNumber = loginViewModel.tokenManager.getUserData()?.mobile.orEmpty(),
                image = loginViewModel.tokenManager.getUserData()?.image.orEmpty(),
                state = checkState,
                onChangeSpecialRequest = { checkOutViewModel.handleIntents(CheckOutIntents.OnChangeSpecialRequest(it)) },
                onBackedBresed = { navController.popBackStack() },
                subRegion = subRegion,
                allAddress = allAddress,
                onSelectedPayemt = { selectedPayment = it },
                selectedPayemnt = selectedPayment,
                onChangePaymentId = { paymentId = it },
                finishOrder = { cartId ->
                    val (orderDate, orderTime) = parseDateTime(selectedDeliveryTime)
                    checkOutViewModel.handleIntents(
                        CheckOutIntents.FinishOrder(
                            cartId = cartId,
                            paymentMethodId = paymentId,
                            specialRequest = checkState.specialRequest,
                            userId = loginViewModel.tokenManager.getUserData()?.id.toString(),
                            is_preorder = selectedPreOrder,
                            order_time = orderTime,
                            order_date = orderDate
                        )
                    )
                },
                addressList = addresses,
                onAddressSelected = { district, details ->
                    subRegion = district
                    allAddress = details
                },
                onClickToAddNewAddress = {
                    val screenType = "checkOutScreen"
                    navController.navigate(Screen.AllAddressesScreen.route.replace("{screenType}", screenType))
                },
                pickUpStatus = deliveryStatus,
                branchName = if (loginViewModel.tokenManager.getLanguage() == "en")
                    checkState.checkOutResponse?.branch?.titleEn
                else
                    checkState.checkOutResponse?.branch?.titleAr,
                branchAddress = if (loginViewModel.tokenManager.getLanguage() == "en")
                    checkState.checkOutResponse?.branch?.addressEn
                else
                    checkState.checkOutResponse?.branch?.addressAr,
                deliveryTime = deliveryTime.orEmpty(),
                dateTimePicker = selectedDeliveryTime,
                onSelectedOredrNow = { order, preOrderType ->
                    selectedOrder = order
                    selectedPreOrder = preOrderType
                },
                selctedOrder = selectedOrder,
                onNavToTimeDatePickerScreen = { navController.navigate(Screen.DateTimePicker.route) },
                listState = listState,
                languge = loginViewModel.tokenManager.getLanguage()?:"en",
                onShowEnterAddress = { value->
                    showEnterAddressDialoge = value
                },
                vatCost = checkState.checkOutResponse?.cart?.vatCost.toString(),
                serviceChargeCost = checkState.checkOutResponse?.cart?.serviceChargeCost.toString(),
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

        if (showEnterAddressDialoge){
            AlertDialog(
                onDismissRequest = {
                    showEnterAddressDialoge = false
                },
                icon = {

                },
                title = {
                    Text(
                        text = stringResource(R.string.you_should_add_your_address_first),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                text = {

                },
                confirmButton = {
                    CustomButton(
                        onClick = {


                            showEnterAddressDialoge = false

                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.confirm),
                    )
                },
                containerColor = Color.White
            )
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
    selctedOrder: String,
    onSelectedPayemt: (String) -> Unit,
    onSelectedOredrNow: (String, String) -> Unit,
    subRegion: String,
    allAddress: String,
    state: CheckOutStateModel,
    onChangeSpecialRequest: (String) -> Unit,
    name: String,
    phoneNumber: String,
    image: String? = null,
    onBackedBresed: () -> Unit,
    onClickToAddNewAddress: () -> Unit,
    pickUpStatus: Boolean,
    branchName: String?,
    branchAddress: String?,
    deliveryTime: String,
    dateTimePicker: String,
    onNavToTimeDatePickerScreen: () -> Unit,
    listState: LazyListState,
    languge:String,
    onShowEnterAddress: (Boolean) ->Unit,
    serviceChargeCost:String?,
    vatCost:String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        CheckOutHeader(
            userName = name,
            phoneNumber = phoneNumber,
            image = image,
            onBackPressed = onBackedBresed
        )

        // Scrollable content
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SpecialRequestEditText(
                    state = state,
                    onChangeSpecialRequest = onChangeSpecialRequest
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                if (pickUpStatus) {
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
                                .padding(horizontal = 15.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.add_new_address),
                            onClick = onClickToAddNewAddress
                        )
                    } else {
                        CustomButton(
                            modifier = Modifier
                                .padding(horizontal = 15.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.add_new_address),
                            onClick = onClickToAddNewAddress
                        )
                    }
                } else {
                    showBranchDetails(
                        branchName = branchName,
                        branchaddress = branchAddress
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
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
                                    Log.d("payment", payment.id)
                                    onSelectedPayemt(it)
                                    onChangePaymentId(payment.id)
                                }
                            )
                        }
                    }
                }
            }

            if (state.checkOutResponse?.preOrder == 1) {
                item {
                    Spacer(modifier = Modifier.height(15.dp))
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Gray),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        OrderOptions(
                            order = stringResource(R.string.order_now),
                            onSelected = { onSelectedOredrNow(it, "0") },
                            selectedOrder = selctedOrder,
                            timepicker = ""
                        )

                        if (state.checkOutResponse.dateTimeOrder == 1) {
                            OrderOptions(
                                order = stringResource(R.string.order_later),
                                onSelected = {
                                    onSelectedOredrNow(it, "1")
                                    onNavToTimeDatePickerScreen()
                                },
                                selectedOrder = selctedOrder,
                                timepicker = dateTimePicker
                            )
                        }

                        if (state.checkOutResponse.preOrderTextStatus == 1) {
                            OrderOptions(
                                order = if (languge == "en") state.checkOutResponse.preOrderText else state.checkOutResponse.preOrderTextAr,
                                onSelected = { onSelectedOredrNow(it, "2") },
                                selectedOrder = selctedOrder,
                                timepicker = ""
                            )
                        }
                    }
                }
            }


            item {
                Spacer(modifier = Modifier.height(30.dp))  // Add more space before CartBottomBar

                CartBottomBar(
                    priceItems = state.checkOutResponse?.cart?.primaryPrice?.toString() ?: "0.00",
                    deliveryPrice = state.checkOutResponse?.cart?.deliveryCost?.toString()
                        ?: "0.00",
                    totalPrice = state.checkOutResponse?.cart?.totalPrice?.toString() ?: "0.00",
                    buttonText = stringResource(R.string.checkout),
                    deliveryTime = deliveryTime,
                    onPayClick = {
                        if (pickUpStatus) {
                            if (addressList.isEmpty()) {
                                onShowEnterAddress(true)
                            } else {
                                finishOrder(state.checkOutResponse?.cart?.id.orEmpty())
                            }
                        } else {
                            finishOrder(state.checkOutResponse?.cart?.id.orEmpty())
                        }
                    },
                    pickUpStatus = pickUpStatus,
                    vatCost = vatCost,
                    serviceCharcheCost = serviceChargeCost
                )

//                Spacer(modifier = Modifier.height(24.dp))
            }



        }
    }
}

fun parseDateTime(dateTimeString: String): Pair<String, String> {
    if (dateTimeString.isEmpty()) return Pair("", "")

    return try {
        val parts = dateTimeString.split(" - ")
        if (parts.size != 2) return Pair("", "")

        val datePart = parts[0].trim()
        val timePart = parts[1].trim()

        val dateComponents = datePart.split(" ")
        if (dateComponents.size < 4) return Pair("", "")

        val day = dateComponents[1].toIntOrNull() ?: return Pair("", "")
        val monthArabic = dateComponents[2]
        val year = dateComponents[3].toIntOrNull() ?: return Pair("", "")

        val monthsMap = mapOf(
            "يناير" to 1, "فبراير" to 2, "مارس" to 3, "أبريل" to 4,
            "مايو" to 5, "يونيو" to 6, "يوليو" to 7, "أغسطس" to 8,
            "سبتمبر" to 9, "أكتوبر" to 10, "نوفمبر" to 11, "ديسمبر" to 12
        )
        val month = monthsMap[monthArabic] ?: return Pair("", "")
        val formattedDate = String.format("%04d-%02d-%02d", year, month, day)

        val timeComponents = timePart.split(" ")
        if (timeComponents.size != 2) return Pair("", "")

        val timeValue = timeComponents[0]
        val period = timeComponents[1]

        val timeValues = timeValue.split(":")
        if (timeValues.size != 2) return Pair("", "")

        var hour = timeValues[0].toIntOrNull() ?: return Pair("", "")
        val minute = timeValues[1].toIntOrNull() ?: return Pair("", "")

        if (period == "م" && hour != 12) hour += 12
        else if (period == "ص" && hour == 12) hour = 0

        val formattedTime = String.format("%02d:%02d:00", hour, minute)

        Pair(formattedDate, formattedTime)
    } catch (e: Exception) {
        Log.e("parseDateTime", "Error: ${e.message}")
        Pair("", "")
    }
}