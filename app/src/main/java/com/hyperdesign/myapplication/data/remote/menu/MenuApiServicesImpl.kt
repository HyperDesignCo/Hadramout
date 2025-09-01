package com.hyperdesign.myapplication.data.remote.menu

import android.util.Log
import com.hyperdesign.myapplication.data.dto.AddToCartResponseDto
import com.hyperdesign.myapplication.data.dto.CartResponseDto
import com.hyperdesign.myapplication.data.dto.CheckOutResponseDto
import com.hyperdesign.myapplication.data.dto.MealDetailResponseDto
import com.hyperdesign.myapplication.data.dto.MenuResponseDto
import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.Entity.CheckCouponRequest
import com.hyperdesign.myapplication.domain.Entity.CheckOutRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteCartRequest
import com.hyperdesign.myapplication.domain.Entity.ShowCartRequest
import com.hyperdesign.myapplication.domain.Entity.UpdateCartItemQuantityRequest
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

    override suspend fun showCart(showCartRequest: ShowCartRequest): CartResponseDto {
        return try {
            val response =client.post("showCart"){
                contentType(ContentType.Application.Json)
                setBody(showCartRequest)
            }.body<CartResponseDto>()
            Log.d("MenuApiServices", "Menu Response for branchId=$showCartRequest: $response")
            response
        }catch (e:Exception){
            Log.e("MenuApiServices", "getMenus failed for branchId=$showCartRequest: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteCartItem(deleteCartRequest: DeleteCartRequest): CartResponseDto {
        return try {
            val response = client.post("deleteCartItem") {
                contentType(ContentType.Application.Json)
                setBody(deleteCartRequest)
            }.body<CartResponseDto>()
            Log.d("MenuApiServices", "deleteCart Response for branchId=$deleteCartRequest: $response")

            response

        }catch (e: Exception){
            Log.e("MenuApiServices", "deleteCart failed for branchId=$deleteCartRequest: ${e.message}")

            throw e
        }
    }

    override suspend fun updateCartItemQuantity(updateCartItemQuantityRequest: UpdateCartItemQuantityRequest): CartResponseDto {
        return try {
            val response =client.post("updateCartItemQuantity") {
                contentType(ContentType.Application.Json)
                setBody(updateCartItemQuantityRequest)
            }.body<CartResponseDto>()
            Log.d("MenuApiServices", "updateCartItemQuantity Response for branchId=$updateCartItemQuantityRequest: $response")

            response

        }catch (e: Exception){
            Log.e("MenuApiServices", "updateCartItem failed for branchId=$updateCartItemQuantityRequest: ${e.message}")

            throw e
        }
    }

    override suspend fun checkCouponCode(checkCouponRequest: CheckCouponRequest): AddToCartResponseDto {
        return try {
            val response = client.post("checkCoupon") {
                contentType(ContentType.Application.Json)
                setBody(checkCouponRequest)
            }.body<AddToCartResponseDto>()

            Log.d("MenuApiServices", "checkCoupon Response for branchId=$checkCouponRequest: $response")

            response

        }catch (e: Exception){
            Log.e("MenuApiServices", "checkCoupon failed for branchId=$checkCouponRequest: ${e.message}")

            throw e
        }
    }

    override suspend fun checkout(checkOutRequest: CheckOutRequest): CheckOutResponseDto {
        return try {
            val response = client.post("checkout") {
                contentType(ContentType.Application.Json)
                setBody(checkOutRequest)
            }.body<CheckOutResponseDto>()
            Log.d("MenuApiServices", "checkout Response for branchId=$checkOutRequest: $response")

            response

        }catch (e: Exception){
            Log.e("MenuApiServices", "checkOut failed for branchId=$checkOutRequest: ${e.message}")

            throw e
        }
    }


}