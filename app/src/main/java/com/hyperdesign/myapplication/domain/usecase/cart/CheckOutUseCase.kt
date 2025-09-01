package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.CheckOutRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class CheckOutUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(checkOutRequest: CheckOutRequest)=menuRepo.checkout(checkOutRequest)
}