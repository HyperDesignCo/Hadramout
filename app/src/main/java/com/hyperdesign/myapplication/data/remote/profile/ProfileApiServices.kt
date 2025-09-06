package com.hyperdesign.myapplication.data.remote.profile

import com.hyperdesign.myapplication.data.dto.AboutUsResponseDTO
import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.OrdersResponseDTO
import com.hyperdesign.myapplication.data.dto.PagesResponseDto
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest

interface ProfileApiServices {

    suspend fun showMyOrders(type:Int): OrdersResponseDTO
    suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseDto

    suspend fun displayAbouUS(): AboutUsResponseDTO

    suspend fun showPage(page:Int): PagesResponseDto
}