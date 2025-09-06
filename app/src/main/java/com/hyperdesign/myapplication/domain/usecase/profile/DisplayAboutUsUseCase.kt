package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class DisplayAboutUsUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke()= profileRepo.displayAboutUS()

}