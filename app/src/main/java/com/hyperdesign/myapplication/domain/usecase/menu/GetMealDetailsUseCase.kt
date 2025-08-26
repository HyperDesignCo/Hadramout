package com.hyperdesign.myapplication.domain.usecase.menu

import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class GetMealDetailsUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(branchId: Int, mealId: Int) = menuRepo.showMealDetails(branchId, mealId)
}