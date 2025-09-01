package com.hyperdesign.myapplication.domain.Entity


data class CheckOutResponseEntity(
    val cart: CartEntity,
    val message: String,
    val addresses: List<AddressEntity>,
    val payment_methods: List<PaymentMethodEntity>
)

data class PaymentMethodEntity(
    val id: String,
    val title: String,
    val image: String,
    val show_android: String,
    val show_apple: String
)

data class CheckOutRequest(
    val branch_id:String
)