package com.hyperdesign.myapplication.data.mapper.profile

import com.hyperdesign.myapplication.data.dto.GetProfileResponseDTO
import com.hyperdesign.myapplication.data.dto.ProfileUserDTO
import com.hyperdesign.myapplication.data.dto.WalletTransactionDTO
import com.hyperdesign.myapplication.domain.Entity.GetProfileEntity
import com.hyperdesign.myapplication.domain.Entity.ProfileUserEntity
import com.hyperdesign.myapplication.domain.Entity.WalletTransactionEntity

fun GetProfileResponseDTO?.toEntity(): GetProfileEntity {
    return GetProfileEntity(
        phone = this?.phone,
        pointValue = this?.pointValue,
        user = this?.user?.toEntity(),
        walletTransactions = this?.walletTransactions?.map { it.toEntity() } ?: emptyList(),
        message = this?.message
    )
}

fun ProfileUserDTO?.toEntity(): ProfileUserEntity? {
    return this?.let {
        ProfileUserEntity(
            id = it.id,
            name = it.name,
            email = it.email,
            image = it.image,
            mobile = it.mobile,
            balance = it.balance,
            authenticated = it.authenticated
        )
    }
}

fun WalletTransactionDTO?.toEntity(): WalletTransactionEntity {
    return WalletTransactionEntity(
        id = this?.id,
        orderId = this?.orderId,
        balance = this?.balance,
        points = this?.points,
        status = this?.status,
        createdAt = this?.createdAt
    )
}
