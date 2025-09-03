package com.hyperdesign.myapplication.presentation.profile.myorders.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.MealOrderEntity
import com.hyperdesign.myapplication.domain.Entity.OrderEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.profile.myorders.mvi.MyOrderViewModel
import com.hyperdesign.myapplication.presentation.profile.myorders.mvi.MyOrdersIntants
import com.hyperdesign.myapplication.presentation.profile.myorders.ui.widgets.OrderItem
import com.hyperdesign.myapplication.presentation.profile.myorders.ui.widgets.ShowNewOrdersOrPreviousOrders
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyOrderScreen(myOrderViewModel: MyOrderViewModel= koinViewModel()){
    val navController = LocalNavController.current
    val orderSate by myOrderViewModel.myOrdersState.collectAsStateWithLifecycle()
    var showOrder by remember { mutableStateOf(true) }
    var reOrderMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    var ordersEntity by remember { mutableStateOf<List<OrderEntity>>(emptyList()) }

    LaunchedEffect(orderSate) {
        ordersEntity = orderSate.MyOrdersResponse?.orders.orEmpty()
        if (reOrderMessage.isEmpty()) {
            reOrderMessage = orderSate.reOrderResponse?.message.orEmpty()
        }
    }

    LaunchedEffect(showOrder) {
        if (showOrder){
            myOrderViewModel.handleIntents(MyOrdersIntants.ShowMyOrders(1))
        }else{
            myOrderViewModel.handleIntents(MyOrdersIntants.ShowMyOrders(2))

        }
    }

    LaunchedEffect(reOrderMessage) {
        if (reOrderMessage.isNotEmpty()){
            if (reOrderMessage=="meals added to cart"){
                Toast.makeText(context,context.getString(R.string.the_meal_has_been_added_to_the_cart),Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.CartScreen.route)
                {
                    popUpTo(Screen.MyOrders.route) { inclusive = true }
                    launchSingleTop = true
                }

                reOrderMessage = ""

            }
            else{
                Toast.makeText(context,orderSate.errorMsg,Toast.LENGTH_SHORT).show()
                reOrderMessage = ""
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MyOrderScreenContent(onBackPressed = {
            navController.popBackStack()
        }, onSelectNewOrPreviousOrder = { bool -> showOrder = bool }, orders = ordersEntity,
            onReOrder = {orderId->myOrderViewModel.handleIntents(MyOrdersIntants.ReOrder(orderId))})

        if (orderSate.isLoading) {
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
fun MyOrderScreenContent(onReOrder:(String)-> Unit, orders: List<OrderEntity>, onBackPressed:()->Unit, onSelectNewOrPreviousOrder:(Boolean)->Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            onBackPressesd = {onBackPressed()},
            showTitle = true,
            title = stringResource(R.string.my_orders),
            showBackPressedIcon = true,
            height = 90
            )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            ShowNewOrdersOrPreviousOrders(onSelectNewOrPreviousOrder = {onSelectNewOrPreviousOrder(it)})

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(orders, key = {order-> order.id}){order->
                    var meal : MealOrderEntity?=null
                   order.meals.forEach { meal=it }
                    meal?.let { OrderItem(order =order, meal = it , onReOrder = {
                        onReOrder(order.id)
                    }) }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
    }
}