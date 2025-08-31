package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.UpdateCartItemQuantityRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class UpdateCartItemQuantityUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(updateCartItemQuantityRequest: UpdateCartItemQuantityRequest)=menuRepo.updateCartItemQuantity(updateCartItemQuantityRequest)
}