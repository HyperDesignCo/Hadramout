package com.hyperdesign.myapplication.domain.usecase.auth

import com.hyperdesign.myapplication.data.remote.model.LoginResponse
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.repo.AuthRepo

class LoginUseCase(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(
       loginRequest: LoginRequest
    ) = authRepository.login(loginRequest)
}