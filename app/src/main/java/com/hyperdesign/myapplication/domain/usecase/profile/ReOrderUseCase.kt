package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.Entity.ReorderRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class ReOrderUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(reOrderRequest: ReorderRequest) = profileRepo.reOrder(reOrderRequest)
}