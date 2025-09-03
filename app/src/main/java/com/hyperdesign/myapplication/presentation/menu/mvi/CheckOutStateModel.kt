package com.hyperdesign.myapplication.presentation.menu.mvi

import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.AddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CheckOutResponseEntity

data class CheckOutStateModel(
    val isLoading: Boolean =false,
    val errorMsg :String? =null,
    val specialRequest:String="",
    val paymentMethodId:String="",
    val finishOrderResponse: AddToCartResponseEntity?=null,
    val checkOutResponse : CheckOutResponseEntity?=null,
    val address: AddressResponseEntity?=null,

)
