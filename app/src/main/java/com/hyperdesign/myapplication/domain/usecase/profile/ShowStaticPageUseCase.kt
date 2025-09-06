package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class ShowStaticPageUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(page:Int)=profileRepo.showPage(page)
}