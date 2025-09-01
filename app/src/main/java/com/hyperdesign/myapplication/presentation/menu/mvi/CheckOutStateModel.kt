package com.hyperdesign.myapplication.presentation.menu.mvi

import com.hyperdesign.myapplication.domain.Entity.AddressResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CheckOutResponseEntity

data class CheckOutStateModel(
    val isLoading: Boolean =false,
    val errorMsg :String? =null,
    val specialRequest:String="",
    val checkOutResponse : CheckOutResponseEntity?=null,
    val address: AddressResponseEntity?=null,

)
