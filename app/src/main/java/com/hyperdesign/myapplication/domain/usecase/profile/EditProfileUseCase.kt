package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.Entity.EditProfileRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class EditProfileUseCase(
    private val profileRepo: ProfileRepo
) {

    suspend operator fun invoke(editProfileRequest: EditProfileRequest)=profileRepo.editProfile(editProfileRequest)
}