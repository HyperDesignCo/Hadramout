package com.hyperdesign.myapplication.data.remote.auth

import com.hyperdesign.myapplication.data.remote.model.LoginResponse
import com.hyperdesign.myapplication.domain.Entity.LoginRequest

interface NetworkingApiServices {

    suspend fun login(loginRequest : LoginRequest): LoginResponse
}