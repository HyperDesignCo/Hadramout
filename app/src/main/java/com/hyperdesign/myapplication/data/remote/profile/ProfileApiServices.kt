package com.hyperdesign.myapplication.data.remote.profile

import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.OrdersResponseDTO
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest

interface ProfileApiServices {

    suspend fun showMyOrders(type:Int): OrdersResponseDTO
    suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseDto
}