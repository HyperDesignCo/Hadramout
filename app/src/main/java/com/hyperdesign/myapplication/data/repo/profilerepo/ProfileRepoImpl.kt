package com.hyperdesign.myapplication.data.repo.profilerepo

import com.hyperdesign.myapplication.data.mapper.menu.toDomain
import com.hyperdesign.myapplication.data.mapper.profile.toEntity
import com.hyperdesign.myapplication.data.remote.profile.ProfileApiServices
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.OrdersResponseEntity
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class ProfileRepoImpl(
    private val profileApiServices: ProfileApiServices
): ProfileRepo {
    override suspend fun showMyOrders(type: Int): OrdersResponseEntity {
        val response = profileApiServices.showMyOrders(type)
        return response.toEntity()
    }

    override suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseEntity {
        val response = profileApiServices.reOrder(reOrderRequest)
        return response.toDomain()
    }
}