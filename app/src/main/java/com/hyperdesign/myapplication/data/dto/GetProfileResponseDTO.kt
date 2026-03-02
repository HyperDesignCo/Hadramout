package com.hyperdesign.myapplication.data.dto

import com.google.gson.annotations.SerializedName

data class GetProfileResponseDTO(
    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("point_value")
    val pointValue: String? = null,

    @SerializedName("user")
    val user: ProfileUserDTO? = null,

    @SerializedName("WalletTransactions")
    val walletTransactions: List<WalletTransactionDTO>? = emptyList(),

    @SerializedName("message")
    val message: String? = null
)

data class ProfileUserDTO(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("mobile")
    val mobile: String? = null,

    @SerializedName("balance")
    val balance: String? = null,

    @SerializedName("authenticated")
    val authenticated: String? = null
)

data class WalletTransactionDTO(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("order_id")
    val orderId: String? = null,

    @SerializedName("balance")
    val balance: String? = null,

    @SerializedName("points")
    val points: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)
