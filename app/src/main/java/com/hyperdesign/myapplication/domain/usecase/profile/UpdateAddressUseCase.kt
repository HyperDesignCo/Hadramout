package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.Entity.updateAddressRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class UpdateAddressUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(updateAddressRequest: updateAddressRequest)=profileRepo.updateAddress(updateAddressRequest)
}