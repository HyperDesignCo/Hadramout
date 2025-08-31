package com.hyperdesign.myapplication.presentation.menu.mvi

sealed class CartIntents {

    data class GetCart(val branchId: Int) : CartIntents()

    data class deleteCartItem(val cartId:String,val itemId :String): CartIntents()

    data class OnChangeQuantity(val newQuantity:String):CartIntents()

    data class IncreaseCartItemQuantity(val cartId:String,val itemId :String,val newQuantity:String):CartIntents()

    data class OnChangeCopounText(val text:String):CartIntents()

    data class OnCkeckCopounClick(val cartId:String,val promoCode:String):CartIntents()
    data class DecreaseCartItemQuantity(val cartId:String,val itemId :String,val newQuantity:String):CartIntents()
}