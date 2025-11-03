package com.hyperdesign.myapplication.presentation.menu.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.Entity.AddToCartResponseEntity
import com.hyperdesign.myapplication.domain.Entity.CheckOutRequest
import com.hyperdesign.myapplication.domain.Entity.FinishOrderRequest
import com.hyperdesign.myapplication.domain.usecase.cart.CheckOutUseCase
import com.hyperdesign.myapplication.domain.usecase.cart.FinishOrderUseCase
import com.hyperdesign.myapplication.domain.usecase.home.GetAllAddressUseCase
import com.hyperdesign.myapplication.presentation.di.viewModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckOutViewModel(
    private val getAllAddressUseCase: GetAllAddressUseCase,
    private val checkOutUseCase: CheckOutUseCase,
    private val finishOrderUseCase: FinishOrderUseCase
): ViewModel() {

    private var _checkOutState = MutableStateFlow(CheckOutStateModel())
    val checkOutState : StateFlow<CheckOutStateModel> =_checkOutState.asStateFlow()

    init {
        getAllAddress()
    }
    fun handleIntents(intent: CheckOutIntents){
        when(intent){
            is CheckOutIntents.GetAddress -> {
                getAllAddress()
            }
            is CheckOutIntents.OnChangeSpecialRequest -> {
                _checkOutState.value = _checkOutState.value.copy(
                    specialRequest = intent.text
                )

            }

            is CheckOutIntents.CheckOutClick -> {
                checkOut(intent.branchId)
            }

            is CheckOutIntents.ChangePaymentMethodId -> {
                _checkOutState.value=_checkOutState.value.copy(
                    paymentMethodId = intent.paymentMethodId
                )
            }
            is CheckOutIntents.FinishOrder ->{
                finishOrder(intent.paymentMethodId,intent.cartId,intent.userId,intent.is_preorder,intent.order_time,intent.order_date)
            }
        }
    }

    private fun finishOrder(paymentMethodId: String,cartId: String, userId: String,is_preorder: String,order_time: String,order_date: String) {
        _checkOutState.value=_checkOutState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val finishOrderRequest = FinishOrderRequest(cartId = cartId, paymentMethodId=paymentMethodId, specialRequest = _checkOutState.value.specialRequest, userAddressId = userId, isPreOrder = is_preorder, orderTime = order_time, orderDate = order_date)
                val response = finishOrderUseCase(finishOrderRequest)
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,
                    finishOrderResponse = response
                )


            }.onSuccess {
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("faild finishOrder",it.message.toString())
            }
        }
    }

    private fun checkOut(branchId: String) {
        _checkOutState.value=_checkOutState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val checkOutRequest = CheckOutRequest(branch_id = branchId)
                val response = checkOutUseCase(checkOutRequest)
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,
                    checkOutResponse =response
                )


            }.onSuccess {
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("faild checkOut",it.message.toString())
            }
        }

    }

    private fun getAllAddress() {
        _checkOutState.value=_checkOutState.value.copy(
            isLoading = false
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getAllAddressUseCase()
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,
                    address = response
                )


            }.onSuccess {
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,

                )

            }.onFailure {
                _checkOutState.value=_checkOutState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("faild to get address",it.message.toString())
            }
        }

    }
}