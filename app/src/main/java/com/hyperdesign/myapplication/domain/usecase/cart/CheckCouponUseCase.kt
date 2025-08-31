package com.hyperdesign.myapplication.domain.usecase.cart

import com.hyperdesign.myapplication.domain.Entity.CheckCouponRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class CheckCouponUseCase(
    private val menuRepo: MenuRepo
) {

    suspend operator fun invoke(checkCouponRequest: CheckCouponRequest) = menuRepo.checkCouponCode(checkCouponRequest)
}