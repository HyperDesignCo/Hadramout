package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class GetAreaUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(regionId:Int) = profileRepo.getArea(regionId)
}