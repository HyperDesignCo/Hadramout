package com.hyperdesign.myapplication.domain.usecase.auth

import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo

class RefreshTokenUseCase(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(refreshTokenRequest: ForgetPasswordRequest) = authRepository.refreshToken(refreshTokenRequest)
}