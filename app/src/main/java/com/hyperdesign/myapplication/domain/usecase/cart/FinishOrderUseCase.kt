package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.FinishOrderRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class FinishOrderUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(finishOrderRequest: FinishOrderRequest)=menuRepo.finishOrder(finishOrderRequest)
}