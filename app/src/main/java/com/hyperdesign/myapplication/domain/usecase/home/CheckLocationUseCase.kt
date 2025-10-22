package com.hyperdesign.myapplication.domain.usecase.home

import com.hyperdesign.myapplication.domain.Entity.checkLocationRequest
import com.hyperdesign.myapplication.domain.repo.home.HomeRepo

class CheckLocationUseCase(
    private val homeRepo: HomeRepo
) {

    suspend operator fun invoke(checkLocationRequest: checkLocationRequest) = homeRepo.checkLocation(checkLocationRequest)
}