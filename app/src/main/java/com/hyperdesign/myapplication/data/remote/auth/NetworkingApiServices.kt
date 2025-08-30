package com.hyperdesign.myapplication.data.remote.auth

import com.hyperdesign.myapplication.data.dto.ForgetPasswordResponseDto
import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.RegisterResponse
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.NewPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst

interface NetworkingApiServices {

    suspend fun login(loginRequest : LoginRequest): LoginResponse

    suspend fun register(registerRequest : RegisterRequst): RegisterResponse

    suspend fun forgetPassword(forgetPasswordRequest : ForgetPasswordRequest): ForgetPasswordResponseDto

    suspend fun refreshToken(refreshTokenRequest:ForgetPasswordRequest):LoginResponse

    suspend fun crateNewPassword(newPasswordRequest: NewPasswordRequest):LoginResponse
}