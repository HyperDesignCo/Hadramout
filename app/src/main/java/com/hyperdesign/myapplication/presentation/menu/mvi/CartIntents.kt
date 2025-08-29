package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class CartIntents {

    data class GetCart(val branchId: Int) : CartIntents()
}