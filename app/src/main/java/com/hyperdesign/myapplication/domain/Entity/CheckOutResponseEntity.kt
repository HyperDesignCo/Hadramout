package com.hyperdesign.myapplication.domain.Entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class CheckOutResponseEntity(
    val branch : BranchInfoEntity?,
    val preOrder:Int,//check to put cart of orders or not
    val dateTimeOrder:Int,//check if orderLater will show or not,
    val preOrderText:String,//display text from backend,
    val preOrderTextStatus:Int, // check if display preOrderText or not
    val cart: CartEntity,
    val message: String,
    val addresses: List<AddressEntity>,
    val payment_methods: List<PaymentMethodEntity>
)

data class BranchInfoEntity(
    val id :Int,
    val titleAr :String?,
    val titleEn :String?,
    val slug :String?,
    val addressEn :String?,
    val addressAr :String?,
    val deliveryCost :String?,
    val lat :String?,
    val lng :String?,
    val status:Int?,
    val busyHours:Int?,
    val busyEndAt :String?,
    val openTime:String?,
    val closeTime:String?,
    val pickupStatus:Int?,
    val pickupBusyHours:String?,
    val createAt:String?,
    val updateAt:String?,
    val deleteAt:String?,
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
    val userAddressId:String,
    @SerializedName("is_preorder")
    val isPreOrder:String,
    @SerializedName("order_time")
    val orderTime:String,
    @SerializedName("order_date")
    val orderDate:String
)