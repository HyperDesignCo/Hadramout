package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.Entity.DeleteAddressRequest
import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class DeleteAddressUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(deleteAddressRequest: DeleteAddressRequest)=profileRepo.deleteAddress(deleteAddressRequest)
}