package com.hyperdesign.myapplication.presentation.menu.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.CheckCouponRequest
import com.hyperdesign.myapplication.domain.Entity.DeleteCartRequest
import com.hyperdesign.myapplication.domain.Entity.ShowCartRequest
import com.hyperdesign.myapplication.domain.Entity.UpdateCartItemQuantityRequest
import com.hyperdesign.myapplication.domain.usecase.cart.CheckCouponUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.DeleteCartItemUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.ShowCartUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.UpdateCartItemQuantityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val showCartUseCase: ShowCartUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val updateCartItemQuantityUseCase: UpdateCartItemQuantityUseCase,
    private val checkCouponUseCase: CheckCouponUseCase,
    val tokenManager: TokenManager
): ViewModel() {

    private var _cartState = MutableStateFlow(MenuStateModel())
    val cartState: StateFlow<MenuStateModel> = _cartState.asStateFlow()

    fun handleIntent(intent: CartIntents){
        when(intent){
            is CartIntents.GetCart -> {
                showCart(intent.branchId)
            }
            is CartIntents.deleteCartItem ->{
                deleteCartItem(
                    intent.cartId,
                    intent.itemId
                )
            }
            is CartIntents.DecreaseCartItemQuantity -> decreaseCartIntemQuantity(intent.cartId, intent.itemId)
            is CartIntents.IncreaseCartItemQuantity -> increaseCartIntemQuantity(intent.cartId, intent.itemId)
            is CartIntents.OnChangeQuantity -> {
                _cartState.value = _cartState.value.copy(
                    quantity = intent.newQuantity
                )

            }

            is CartIntents.OnChangeCopounText -> {
                _cartState.value = _cartState.value.copy(
                    copoun = intent.text
                )
            }
            is CartIntents.OnCkeckCopounClick -> ckeckCopounCode(intent.cartId)
        }
    }

    private fun ckeckCopounCode(cartId: String) {

        _cartState.value = _cartState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val checkCopoanRequest = CheckCouponRequest(cartId = cartId, promoCode = _cartState.value.copoun)
                val response = checkCouponUseCase.invoke(checkCopoanRequest)
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    AddToCartData = response,
                    copounMessage = response.message
                )

            }.onSuccess {
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                )

            }.onFailure {
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    errorMessage = it.message
                )
                Log.e("CartViewModel", "Error fetching cart: ${it.message}")

            }
        }


    }

    private fun increaseCartIntemQuantity(cartId: String, itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val quantity = _cartState.value.quantity.toInt()+1
                val updateRequest = UpdateCartItemQuantityRequest(branchId = tokenManager.getBranchId().toString(),cartId = cartId, itemId = itemId, newQuantity = quantity.toString(), areaId = tokenManager.getAreaId().toString())
                val response = updateCartItemQuantityUseCase.invoke(updateRequest)
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    showCartDate = response
                )


            }.onSuccess {
                _cartState.value = _cartState.value.copy(
                    isLoading = false
                )
            }.onFailure {
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
                Log.e("CartViewModel", "Error fetching cart: ${it.message}")
            }
        }
    }

    private fun decreaseCartIntemQuantity(cartId: String, itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val quantity = _cartState.value.quantity.toInt()-1
                if (quantity<1){
                    deleteCartItem(cartId,itemId)
                }
                val updateRequest = UpdateCartItemQuantityRequest(branchId= tokenManager.getBranchId().toString(),cartId = cartId, itemId = itemId, newQuantity = quantity.toString(), areaId = tokenManager.getAreaId().toString())
                val response = updateCartItemQuantityUseCase.invoke(updateRequest)
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    showCartDate = response
                )


            }.onSuccess {
                _cartState.value = _cartState.value.copy(
                    isLoading = false
                )
            }.onFailure {
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
                Log.e("CartViewModel", "Error fetching cart: ${it.message}")
            }
        }
    }

    private fun deleteCartItem(cartId:String,itemId:String){
        _cartState.value = _cartState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val deleteRequest = DeleteCartRequest(cartId = cartId, itemId = itemId, branchId = tokenManager.getBranchId().toString(), areaId =tokenManager.getAreaId().toString() )
                val response = deleteCartItemUseCase.invoke(deleteRequest)
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    showCartDate = response
                )


            }.onSuccess {
                _cartState.value = _cartState.value.copy(
                    isLoading = false
                )
            }.onFailure {
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
                Log.e("CartViewModel", "Error fetching cart: ${it.message}")
            }
        }
    }
    private fun showCart(branchId: Int) {
        _cartState.value = _cartState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val showCartRequest = ShowCartRequest(branchId.toString(), areaId = tokenManager.getAreaId().toString())
                val response = showCartUseCase.invoke(showCartRequest)
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    showCartDate = response
                )

            }.onSuccess {
                _cartState.value = _cartState.value.copy(
                    isLoading = false
                )
            }.onFailure {
                _cartState.value = _cartState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
                Log.e("CartViewModel", "Error fetching cart: ${it.message}")
            }
        }




    }


}