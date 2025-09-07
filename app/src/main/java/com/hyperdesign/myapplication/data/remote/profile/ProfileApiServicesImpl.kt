package com.hyperdesign.myapplication.data.remote.profile

import android.util.Log
import com.hyperdesign.myapplication.data.dto.AboutUsResponseDTO
import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.AreaResponseDto
import com.hyperdesign.myapplication.data.dto.OrdersResponseDTO
import com.hyperdesign.myapplication.data.dto.PagesResponseDto
import com.hyperdesign.myapplication.data.dto.RegionResponseDto
import com.hyperdesign.myapplication.domain.Entity.CreateNewAddressRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteAddressRequest
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

    override suspend fun displayAbouUS(): AboutUsResponseDTO {
        return try {
            val response = client.get("about_us") {
                contentType(ContentType.Application.Json)

            }.body<AboutUsResponseDTO>()
            Log.d("ProfileApiServices","aboutUSSuccess ,Response:${response}")

            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","aboutUSFaild:${e.message.toString()}")
            throw e
        }
    }

    override suspend fun showPage(page: Int): PagesResponseDto {
        return try {
            val response = client.get("show_page/$page}") {
                contentType(ContentType.Application.Json)

            }.body<PagesResponseDto>()
            Log.d("ProfileApiServices","showPageSuccess ,Response:${response}")
            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","showPageFaild:${e.message.toString()}")
            throw e
        }
    }

    override suspend fun getRegions(): RegionResponseDto {
        return try {
            val response = client.get("get_regions") {
                contentType(ContentType.Application.Json)

            }.body<RegionResponseDto>()
            Log.d("ProfileApiServices","getRegionsSuccess ,Response:${response}")
            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","getRegionsFaild:${e.message.toString()}")
            throw e
        }
    }

    override suspend fun getArea(regionId: Int): AreaResponseDto {
        return try {
            val response = client.get("get_areas") {
                contentType(ContentType.Application.Json)
                parameter("region_id",regionId)

            }.body<AreaResponseDto>()
            Log.d("ProfileApiServices","getAreaSuccess ,Response:${response}")
            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","getAreaFaild:${e.message.toString()}")
            throw e
        }
    }

    override suspend fun createNewAddress(createNewAddressRequest: CreateNewAddressRequest): AddToCartResponseDto {
        return try {
            val response = client.post("create_address") {
                contentType(ContentType.Application.Json)
                setBody(createNewAddressRequest)

            }.body<AddToCartResponseDto>()
            Log.d("ProfileApiServices","createNewAddressSuccess ,Response:${response}")
            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","createNewAddressFaild:${e.message.toString()}")
            throw e
        }
    }

    override suspend fun deleteAddress(deleteAddressRequest: DeleteAddressRequest): AddToCartResponseDto {
        return try {
            val response = client.post("delete_address") {
                contentType(ContentType.Application.Json)
                setBody(deleteAddressRequest)

            }.body<AddToCartResponseDto>()
            Log.d("ProfileApiServices","deleteAddressSuccess ,Response:${response}")
            response
        }catch (e: Exception){
            Log.e("ProfileApiServices","deleteAddressFaild:${e.message.toString()}")
            throw e
        }
    }


}