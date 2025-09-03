package com.hyperdesign.myapplication.data.remote.profile

import android.util.Log
import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.OrdersResponseDTO
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProfileApiServicesImpl(
    private val client: HttpClient
):ProfileApiServices {
    override suspend fun showMyOrders(type: Int): OrdersResponseDTO {
        return try {
            val response = client.get("my_orders") {
                contentType(ContentType.Application.Json)
                parameter("type",type)
            }.body<OrdersResponseDTO>()
            Log.e("ProfileApiServices","MyOrdersSuccess ,MyOrderRequest :Response:${response}")

            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","MyOrdersFaild ,MyOrderRequest :${e.message.toString()}")
            throw e
        }
    }

    override suspend fun reOrder(reOrderRequest: ReorderRequest): AddToCartResponseDto {
        return try {
            val response = client.post("reorder") {
                contentType(ContentType.Application.Json)
                setBody(reOrderRequest)
            }.body<AddToCartResponseDto>()
            Log.e("ProfileApiServices","reOrderSuccess ,reOrderRequest:$reOrderRequest :Response:${response}")

            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","reOrderFaild ,reOrderRequest:$reOrderRequest :${e.message.toString()}")
           throw e
        }
    }


}