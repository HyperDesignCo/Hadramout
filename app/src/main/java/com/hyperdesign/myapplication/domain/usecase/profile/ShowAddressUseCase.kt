package com.hyperdesign.myapplication.domain.usecase.profile

import com.hyperdesign.myapplication.domain.repo.profile.ProfileRepo

class ShowAddressUseCase(
    private val profileRepo: ProfileRepo
) {
    suspend operator fun invoke(addressId:Int) = profileRepo.showAddress(addressId = addressId)
}