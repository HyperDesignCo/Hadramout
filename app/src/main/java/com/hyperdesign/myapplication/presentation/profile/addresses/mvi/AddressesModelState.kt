package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import com.hyperdesign.myapplication.domain.Entity.AddressResponseEntity

data class AddressesModelState(

    val isLoading: Boolean =false,
    val errorMsg :String? =null,
    val addresses: AddressResponseEntity?=null,

    )
