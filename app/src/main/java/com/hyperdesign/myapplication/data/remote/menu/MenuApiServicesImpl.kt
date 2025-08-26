package com.hyperdesign.myapplication.data.remote.menu

import android.util.Log
import com.hyperdesign.myapplication.data.dto.MenuResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MenuApiServicesImpl(
    private val client: HttpClient
): MenuApiServices {
    override suspend fun getMenus(branchId: Int): MenuResponseDto {
       return try {

            val response = client.get("menu") {
                contentType(ContentType.Application.Json)
                parameter("branch_id", branchId)
            }.body<MenuResponseDto>()
           Log.d("MenuApiServices", "Menu Response for branchId=$branchId: $response")
           response
        }catch (e:Exception){
            Log.e("MenuApiServices", "getMenus failed for branchId=$branchId: ${e.message}", e)
           throw e

        }

    }


}