package com.hyperdesign.myapplication.domain.usecase.home

import com.hyperdesign.myapplication.domain.repo.home.HomeRepo

class GetHomeMenueUseCase(
    private val homeRepo: HomeRepo
) {
    suspend operator fun invoke(branchId: Int) = homeRepo.getHomeMenues(branchId)
}