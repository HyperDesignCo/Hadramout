package com.hyperdesign.myapplication.data.remote.home

import android.util.Log
import com.hyperdesign.myapplication.data.dto.AddressResponseDto
import com.hyperdesign.myapplication.data.dto.BranchesResponseDTO
import com.hyperdesign.myapplication.data.dto.HomeResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

class HomeApiServicesImpl(
    val client: HttpClient
): HomeApiServices {
    override suspend fun getBranches(): BranchesResponseDTO {
        return client.get("branches"){
            contentType(ContentType.Application.Json)

        }.body()
    }

    override suspend fun getHomeMenues(branchId: Int): HomeResponseDTO {
        return try {
            val response = client.get("home") {
                contentType(ContentType.Application.Json)
                parameter("branch_id", branchId)
            }.body<HomeResponseDTO>()
            Log.d("HomeApiServices", "Home Response for branchId=$branchId: $response")
            response
        } catch (e: Exception) {
            Log.e("HomeApiServices", "getHomeMenues failed for branchId=$branchId: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getAddress(): AddressResponseDto {
        return try {
            val response = client.post("my_addresses") {
                contentType(ContentType.Application.Json)
            }.body<AddressResponseDto>()
            Log.d("HomeApiServices", "AddressResponse : $response")
            response
        }catch (e: Exception) {
            Log.e("HomeApiServices", "AddressResponse failed = ${e.message}", e)
            throw e
        }
    }

}