package com.hyperdesign.myapplication.data.dto

data class CheckOutResponseDto(
    val cart: CartDto,
    val message: String,
    val addresses: List<AddressDto>,
    val payment_methods: List<PaymentMethodDto>
)



data class PaymentMethodDto(
    val id: String,
    val title: String,
    val image: String,
    val show_android: String,
    val show_apple: String
)