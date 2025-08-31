package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.DeleteCartRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class DeleteCartItemUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(deleteCartRequest: DeleteCartRequest)=menuRepo.deleteCartItem(deleteCartRequest)
}