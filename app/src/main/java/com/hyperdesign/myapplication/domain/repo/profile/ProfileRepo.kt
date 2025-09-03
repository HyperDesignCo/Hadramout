package com.hyperdesign.myapplication.domain.repo.profile

import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.OrdersResponseEntity
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest

interface ProfileRepo {

    suspend fun showMyOrders(type:Int): OrdersResponseEntity
    suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseEntity
}