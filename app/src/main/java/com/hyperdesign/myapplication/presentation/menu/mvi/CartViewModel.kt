package com.hyperdesign.myapplication.presentation.menu.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.Entity.ShowCartRequest
import com.hyperdesign.myapplication.domain.usecase.cart.ShowCartUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val showCartUseCase: ShowCartUseCase
): ViewModel() {

    private var _cartState = MutableStateFlow(MenuStateModel())
    val cartState: StateFlow<MenuStateModel> = _cartState.asStateFlow()

    fun handleIntent(intent: CartIntents){
        when(intent){
            is CartIntents.GetCart -> {
                showCart(intent.branchId)
            }
        }
    }

    private fun showCart(branchId: Int) {
        _cartState.value = _cartState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val showCartRequest = ShowCartRequest(branchId.toString())
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