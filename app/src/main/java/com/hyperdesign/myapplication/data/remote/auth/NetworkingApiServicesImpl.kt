package com.hyperdesign.myapplication.data.remote.auth

import android.util.Log
import com.hyperdesign.myapplication.data.dto.ForgetPasswordResponseDto
import com.hyperdesign.myapplication.data.dto.LoginResponse
import com.hyperdesign.myapplication.data.dto.RegisterResponse
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.Entity.NewPasswordRequest
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NetworkingApiServicesImpl(
    val client: HttpClient
): NetworkingApiServices {

    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return try {
            val response = client.post("user/login"){
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }.body<LoginResponse>()
            Log.d("NetworkingApiServices", "login Response for loginRequest=$loginRequest: $response")
            response

        }catch (e:Exception){
            Log.e("NetworkingApiServices", "login failed for loginRequest=$loginRequest: ${e.message}", e)
            throw e

        }
    }

    override suspend fun register(registerRequest: RegisterRequst): RegisterResponse {
        return client.post("user/register"){
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }.body()
    }

    override suspend fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): ForgetPasswordResponseDto {
        return try {
            val response = client.post("user/forget_password") {
                contentType(ContentType.Application.Json)
                setBody(forgetPasswordRequest)
            }.body<ForgetPasswordResponseDto>()
            Log.d("NetworkingApiServices", "forgetPassword Response for forgetPasswordRequest=$forgetPasswordRequest: $response")

            response

        }catch (e:Exception){
            Log.e("NetworkingApiServices", "forgetPassword failed for forgetPasswordRequest=$forgetPasswordRequest: ${e.message}", e)

            throw e
        }
    }

    override suspend fun refreshToken(refreshTokenRequest: ForgetPasswordRequest): LoginResponse {
        return try {
            val response = client.post("refresh_token") {
                contentType(ContentType.Application.Json)
                setBody(refreshTokenRequest)
            }.body<LoginResponse>()
            Log.d("NetworkingApiServices", "refreshToken Response for refreshTokenRequest=$refreshTokenRequest: $response")
            response

        }catch (e:Exception){
            Log.e("NetworkingApiServices", "refreshToken failed for refreshTokenRequest=$refreshTokenRequest: ${e.message}", e)
            throw e
        }
    }

    override suspend fun crateNewPassword(newPasswordRequest: NewPasswordRequest): LoginResponse {
        return try {
            val response = client.post("user/new_password") {
                contentType(ContentType.Application.Json)
                setBody(newPasswordRequest)
            }.body<LoginResponse>()
            Log.d("NetworkingApiServices", "createPassword Response for createPasswordRequest=$newPasswordRequest: $response")

            response

        }catch (e: Exception){
            Log.e("NetworkingApiServices", "createPassword failed for createPasswordRequest=$newPasswordRequest: ${e.message}", e)
            throw e
        }
    }


}