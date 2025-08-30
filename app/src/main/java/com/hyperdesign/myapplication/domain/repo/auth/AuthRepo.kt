package com.hyperdesign.myapplication.domain.repo.auth

import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordModeEntity
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.NewPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterModelEntity
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst

interface AuthRepo {
    suspend fun login(loginRequest: LoginRequest): LoginEntity

    suspend fun register(registerRequest: RegisterRequst): RegisterModelEntity

    suspend fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): ForgetPasswordModeEntity

    suspend fun refreshToken(refreshTokenRequest: ForgetPasswordRequest): LoginEntity

    suspend fun createNewPassword(newPasswordRequest: NewPasswordRequest): LoginEntity
}