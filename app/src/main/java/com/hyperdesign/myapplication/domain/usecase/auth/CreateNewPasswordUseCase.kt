package com.hyperdesign.myapplication.domain.usecase.auth

import com.hyperdesign.myapplication.domain.Entity.NewPasswordRequest
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo

class CreateNewPasswordUseCase(
    private val authRepo: AuthRepo
) {
    suspend operator fun invoke(newPasswordRequest: NewPasswordRequest)=authRepo.createNewPassword(newPasswordRequest)
}