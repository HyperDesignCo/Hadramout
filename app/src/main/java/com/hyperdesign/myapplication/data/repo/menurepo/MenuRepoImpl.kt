package com.hyperdesign.myapplication.data.repo.menurepo

import com.hyperdesign.myapplication.data.mapper.menu.toDomain
import com.hyperdesign.myapplication.data.mapper.menu.toEntity
import com.hyperdesign.myapplication.data.remote.menu.MenuApiServices
import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CheckCouponRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteCartRequest
import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse
import com.hyperdesign.myapplication.domain.Entity.ShowCartRequest
import com.hyperdesign.myapplication.domain.Entity.UpdateCartItemQuantityRequest
import com.hyperdesign.myapplication.domain.repo.menu.MenuRepo

class MenuRepoImpl(
    private val menuApiServices: MenuApiServices
): MenuRepo {
    override suspend fun getMenus(branchId: Int): MenueResponse {
        val response = menuApiServices.getMenus(branchId)
        return response.toDomain()
    }

    override suspend fun showMealDetails(
        branchId: Int,
        mealId: Int
    ): MealDetailsResponseEntity {
        val response = menuApiServices.showMealDetails(branchId, mealId)
        return response.toDomain()
    }

    override suspend fun addMealToCart(addToCartRequest: AddOrderRequest): AddToCartResponseEntity {
        val response = menuApiServices.addMealToCart(addToCartRequest)
        return response.toDomain()
    }

    override suspend fun showCart(showCartRequest: ShowCartRequest): CartResponseEntity {
        val response = menuApiServices.showCart(showCartRequest)
        return response.toEntity()
    }

    override suspend fun deleteCartItem(deleteCartRequest: DeleteCartRequest): CartResponseEntity {
        val response = menuApiServices.deleteCartItem(deleteCartRequest)
        return response.toEntity()
    }

    override suspend fun updateCartItemQuantity(updateCartItemQuantityRequest: UpdateCartItemQuantityRequest): CartResponseEntity {
        val response = menuApiServices.updateCartItemQuantity(updateCartItemQuantityRequest)
        return response.toEntity()
    }

    override suspend fun checkCouponCode(checkCouponRequest: CheckCouponRequest): AddToCartResponseEntity {
        val response = menuApiServices.checkCouponCode(checkCouponRequest)
        return response.toDomain()
    }
}