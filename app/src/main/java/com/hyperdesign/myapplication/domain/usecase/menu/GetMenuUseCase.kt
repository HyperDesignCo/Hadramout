package com.hyperdesign.myapplication.domain.usecase.menu

import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class GetMenuUseCase(
    private val menuRepo: MenuRepo
) {
    suspend operator fun invoke(branchId: Int) = menuRepo.getMenus(branchId)
}