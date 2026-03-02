package com.hyperdesign.myapplication.domain.Entity

data class GetProfileEntity(
    val phone: String?,
    val pointValue: String?,
    val user: ProfileUserEntity?,
    val walletTransactions: List<WalletTransactionEntity>,
    val message: String?
)

data class ProfileUserEntity(
    val id: String?,
    val name: String?,
    val email: String?,
    val image: String?,
    val mobile: String?,
    val balance: String?,
    val authenticated: String?
)

data class WalletTransactionEntity(
    val id: String?,
    val orderId: String?,
    val balance: String?,
    val points: String?,
    val status: String?,
    val createdAt: String?
)
