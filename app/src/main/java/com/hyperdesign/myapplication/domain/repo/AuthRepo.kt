package com.hyperdesign.myapplication.domain.repo

import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest

interface AuthRepo {
    suspend fun login(loginRequest: LoginRequest): LoginEntity
}