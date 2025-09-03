package com.hyperdesign.myapplication.presentation.profile.myorders.mvi

sealed class MyOrdersIntants {

    data class ShowMyOrders(val type:Int): MyOrdersIntants()
    data class ReOrder(val orderId:String): MyOrdersIntants()
}