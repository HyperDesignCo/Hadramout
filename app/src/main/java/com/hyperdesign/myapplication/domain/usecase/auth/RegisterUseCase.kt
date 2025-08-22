package com.hyperdesign.myapplication.domain.usecase.auth

import com.hyperdesign.myapplication.domain.Entity.RegisterRequst
import com.hyperdesign.myapplication.domain.repo.AuthRepo

class RegisterUseCase(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(registerRequst: RegisterRequst) = authRepository.register(registerRequst)
}