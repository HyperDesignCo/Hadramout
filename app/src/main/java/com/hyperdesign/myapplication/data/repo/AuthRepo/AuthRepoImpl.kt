package com.hyperdesign.myapplication.data.repo.AuthRepo
import com.hyperdesign.myapplication.data.mapper.auth.toLoginDomain
import com.hyperdesign.myapplication.data.remote.auth.NetworkingApiServices
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.repo.AuthRepo

class AuthRepoImpl(
    private val authService: NetworkingApiServices
): AuthRepo {
    override suspend fun login(loginRequest: LoginRequest): LoginEntity {
        val response = authService.login(loginRequest)

        return response.toLoginDomain()

    }
}