package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.AddWalletDiscountRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class AddWalletDiscountUseCase(private val menuRepo: MenuRepo) {
    suspend operator fun invoke(request: AddWalletDiscountRequest) =
        menuRepo.addWalletDiscount(request)
}
