package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class CheckOutIntents {

    data object GetAddress: CheckOutIntents()
    data class OnChangeSpecialRequest(val text:String): CheckOutIntents()

    data class CheckOutClick(val branchId:String):CheckOutIntents()

    data class ChangePaymentMethodId(val paymentMethodId:String): CheckOutIntents()
    data class FinishOrder(val cartId:String,val paymentMethodId:String,val specialRequest:String,val userId:String): CheckOutIntents()
}