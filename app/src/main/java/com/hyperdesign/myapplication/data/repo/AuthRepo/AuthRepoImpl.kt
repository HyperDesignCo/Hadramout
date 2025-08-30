package com.hyperdesign.myapplication.data.repo.AuthRepo
import com.hyperdesign.myapplication.data.mapper.auth.toDomain
import com.hyperdesign.myapplication.data.mapper.auth.toLoginDomain
import com.hyperdesign.myapplication.data.mapper.auth.toRegisterDomain
import com.hyperdesign.myapplication.data.remote.auth.NetworkingApiServices
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordModeEntity
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.NewPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterModelEntity
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst
import com.hyperdesign.myapplication.domain.repo.auth.AuthRepo

class AuthRepoImpl(
    private val authService: NetworkingApiServices
): AuthRepo {
    override suspend fun login(loginRequest: LoginRequest): LoginEntity {
        val response = authService.login(loginRequest)

        return response.toLoginDomain()

    }

    override suspend fun register(registerRequest: RegisterRequst): RegisterModelEntity {
        val response = authService.register(registerRequest)

        return response.toRegisterDomain()
    }

    override suspend fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): ForgetPasswordModeEntity {
        val response = authService.forgetPassword(forgetPasswordRequest)
        return response.toDomain()
    }

    override suspend fun refreshToken(refreshTokenRequest: ForgetPasswordRequest): LoginEntity {
        val response = authService.refreshToken(refreshTokenRequest)
        return response.toLoginDomain()
    }

    override suspend fun createNewPassword(newPasswordRequest: NewPasswordRequest): LoginEntity {
        val response = authService.crateNewPassword(newPasswordRequest)
        return  response.toLoginDomain()
    }
}