package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.ShowCartRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class ShowCartUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(showCartRequest: ShowCartRequest) = menuRepo.showCart(showCartRequest)
}