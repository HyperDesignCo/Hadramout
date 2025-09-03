package com.hyperdesign.myapplication.presentation.profile.myorders.mvi

import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.OrdersResponseEntity

data class MyOrdersModelState(
    val isLoading : Boolean = false,
    val errorMsg:String ="",
    val reOrderResponse : AddToCartResponseEntity?=null,
    val MyOrdersResponse : OrdersResponseEntity ? =null
)
