package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.Entity.CreateNewAddressRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class CreateNewAddressUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(createNewAddressRequest: CreateNewAddressRequest) = profileRepo.createNewAddress(createNewAddressRequest)
}