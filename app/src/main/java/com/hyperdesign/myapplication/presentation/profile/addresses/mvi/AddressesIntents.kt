package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutIntents

sealed class AddressesIntents {

    data object GetAddress: AddressesIntents()

}