package com.hyperdesign.myapplication.domain.repo.menu

import com.hyperdesign.myapplication.data.dto.CartResponseDto
import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CheckCouponRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteCartRequest
import com.hyperdesign.myapplication.domain.Entity.MealDetailsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.MenueResponse
import com.hyperdesign.myapplication.domain.Entity.ShowCartRequest
import com.hyperdesign.myapplication.domain.Entity.UpdateCartItemQuantityRequest

interface MenuRepo {
    suspend fun getMenus(branchId: Int): MenueResponse

    suspend fun showMealDetails(branchId: Int, mealId: Int): MealDetailsResponseEntity

    suspend fun addMealToCart(addToCartRequest: AddOrderRequest): AddToCartResponseEntity

    suspend fun showCart(showCartRequest: ShowCartRequest): CartResponseEntity

    suspend fun deleteCartItem(deleteCartRequest: DeleteCartRequest):CartResponseEntity

    suspend fun updateCartItemQuantity(updateCartItemQuantityRequest: UpdateCartItemQuantityRequest):CartResponseEntity

    suspend fun checkCouponCode(checkCouponRequest: CheckCouponRequest):AddToCartResponseEntity
}