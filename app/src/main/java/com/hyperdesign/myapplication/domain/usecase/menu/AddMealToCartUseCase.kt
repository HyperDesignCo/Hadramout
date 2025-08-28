package com.hyperdesign.myapplication.domain.usecase.menu

import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class AddMealToCartUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(addToCartRequest: AddOrderRequest) = menuRepo.addMealToCart(addToCartRequest)
}