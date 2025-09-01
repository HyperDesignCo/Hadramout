package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class CheckOutIntents {

    data object GetAddress: CheckOutIntents()
    data class OnChangeSpecialRequest(val text:String): CheckOutIntents()

    data class CheckOutClick(val branchId:String):CheckOutIntents()
}