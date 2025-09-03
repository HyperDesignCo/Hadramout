package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


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

data class FinishOrderRequest(
    @SerializedName("cart_id")
    val cartId:String,
    @SerializedName("payment_method_id")
    val paymentMethodId:String,
    @SerializedName("special_requests")
    val specialRequest:String,
    @SerializedName("user_address_id")
    val userAddressId:String
)