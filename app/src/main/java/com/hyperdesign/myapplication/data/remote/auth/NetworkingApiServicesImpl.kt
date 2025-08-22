package com.hyperdesign.myapplication.data.remote.auth

import com.hyperdesign.myapplication.data.remote.dto.LoginResponse
import com.hyperdesign.myapplication.data.remote.dto.RegisterResponse
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
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
        return client.post("user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)

        }.body()
    }

    override suspend fun register(registerRequest: RegisterRequst): RegisterResponse {
        return client.post("user/register"){
            contentType(ContentType.Application.Json)
            setBody(registerRequest)
        }.body()
    }


}