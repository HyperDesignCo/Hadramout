package com.hyperdesign.myapplication.domain.usecase.auth

import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo

class ForgetPasswordUseCase(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(forgetPasswordRequest: ForgetPasswordRequest) = authRepository.forgetPassword(forgetPasswordRequest)
}