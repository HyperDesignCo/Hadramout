package com.hyperdesign.myapplication.data.remote.menu

import android.util.Log
import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.MealDetailResponseDto
import com.hyperdesign.myapplication.data.dto.MenuResponseDto
import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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

    override suspend fun showMealDetails(
        branchId: Int,
        mealId: Int
    ): MealDetailResponseDto {
        return try {
            val response = client.get("show_meal") {
                contentType(ContentType.Application.Json)
                parameter("branch_id", branchId)
                parameter("meal_id", mealId)
            }.body<MealDetailResponseDto>()
            Log.d("MenuApiServices", "Menu Response for branchId=$branchId: $response")
            response

        }catch (e:Exception){
            Log.e("MenuApiServices", "getMenus failed for branchId=$branchId: ${e.message}")
            throw e
        }
    }

    override suspend fun addMealToCart(addToCartRequest: AddOrderRequest): AddToCartResponseDto {
        return try {
            val response =client.post("addToCart"){
                contentType(ContentType.Application.Json)
                setBody(addToCartRequest)
            }.body<AddToCartResponseDto>()
            Log.d("MenuApiServices", "Menu Response for branchId=$addToCartRequest: $response")
            response
        }catch (e:Exception){
            Log.e("MenuApiServices", "getMenus failed for branchId=$addToCartRequest: ${e.message}")
            throw e
        }
    }


}