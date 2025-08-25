package com.hyperdesign.myapplication.data.remote.auth

import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.RegisterResponse
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst

interface NetworkingApiServices {

    suspend fun login(loginRequest : LoginRequest): LoginResponse

    suspend fun register(registerRequest : RegisterRequst): RegisterResponse
}