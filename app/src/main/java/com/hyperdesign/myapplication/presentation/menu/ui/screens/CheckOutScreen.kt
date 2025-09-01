package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.CheckOutResponseEntity
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckOutScreen(checkOutViewModel: CheckOutViewModel =koinViewModel(), loginViewModel: LoginViewModel = koinViewModel()){
    val navController = LocalNavController.current
    val checkState by checkOutViewModel.checkOutState.collectAsStateWithLifecycle()
    var subRegion by remember { mutableStateOf("") }
    var allAddress by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        checkOutViewModel.handleIntents(CheckOutIntents.CheckOutClick("2"))
    }
    LaunchedEffect(checkState) {
        subRegion = "${checkState.address?.addresses?.firstOrNull()?.area?.name.orEmpty()},${
            checkState.address?.addresses?.firstOrNull()?.region?.name.orEmpty()}"
        allAddress = "${checkState.address?.addresses?.firstOrNull()?.sub_region.orEmpty()},${checkState.address?.addresses?.firstOrNull()?.street.orEmpty()},${checkState.address?.addresses?.firstOrNull()?.special_sign.orEmpty()},${checkState.address?.addresses?.firstOrNull()?.building_number.orEmpty()},${checkState.address?.addresses?.firstOrNull()?.floor_number.orEmpty()}"

        selectedPayment=checkState.checkOutResponse?.payment_methods?.firstOrNull()?.title.toString()
    }

    Log.d("CheckOutScreen",selectedPayment)

    Box(modifier = Modifier.fillMaxSize()) {
        CheckOutScreenContent(
            name = loginViewModel.tokenManager.getUserData()?.name.orEmpty(),
            phoneNumber = loginViewModel.tokenManager.getUserData()?.mobile.orEmpty(),
            image = loginViewModel.tokenManager.getUserData()?.image.orEmpty(),
            state = checkState,
            onChangeSpecialRequest = {
                checkOutViewModel.handleIntents(
                    CheckOutIntents.OnChangeSpecialRequest(
                        it
                    )
                )
            },
            onBackedBresed = {
                navController.popBackStack()
            },
            subRegion = subRegion,
            allAddress = allAddress,
            onSelectedPayemt = { payment -> selectedPayment = payment },
            selectedPayemnt = selectedPayment.orEmpty()
        )
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
fun CheckOutScreenContent(selectedPayemnt:String,onSelectedPayemt:(String)->Unit,subRegion:String,allAddress:String,state: CheckOutStateModel,onChangeSpecialRequest:(String)->Unit, name:String, phoneNumber:String, image:String?=null, onBackedBresed:()->Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CheckOutHeader(
            userName =name ,
            phoneNumber = phoneNumber,
            image = image,
            onBackPressed =onBackedBresed

        )

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                SpecialRequestEditText(state = state, onChangeSpecialRequest = onChangeSpecialRequest)
                Spacer(modifier = Modifier.height(10.dp))

            }
            item {
                ShowAddress(subRegion,allAddress)
                Spacer(modifier = Modifier.height(10.dp))

            }



                item {
                    Card (
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
                                textAlign = TextAlign.Start,

                                )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = stringResource(R.string.pay_with_card_or_cash),
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Gray,
                                textAlign = TextAlign.Start,

                                )
                            Spacer(modifier = Modifier.height(5.dp))
                            state.checkOutResponse?.payment_methods?.forEach { payment ->

                                PaymentsOption(
                                    payment = payment.title,
                                    selectedPayment = selectedPayemnt,
                                    onSelected = { onSelectedPayemt(it) })

                            }

                        }

                    }


            }
//            items(state.checkOutResponse?.payment_methods.orEmpty(), key = {payment ->payment.id}) {payment->
//                Spacer(modifier = Modifier.height(10.dp))
//
//            }

        }

        CartBottomBar(
            priceItems = state.checkOutResponse?.cart?.primaryPrice.toString(),
            deliveryPrice = state.checkOutResponse?.cart?.deliveryCost.toString(),
            totalPrice = state.checkOutResponse?.cart?.totalPrice.toString(),
            buttonText = stringResource(R.string.checkout),
            onPayClick = {  }
        )


    }
}