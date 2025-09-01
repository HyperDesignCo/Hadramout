package com.hyperdesign.myapplication.domain.usecase.home

import com.hyperdesign.myapplication.domain.repo.home.HomeRepo

class GetAllAddressUseCase(
    private val homeRepo: HomeRepo
) {
    suspend operator fun invoke() = homeRepo.getAddress()
}