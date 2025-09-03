package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class ShowMyOrdersUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(type:Int)= profileRepo.showMyOrders(type)
}