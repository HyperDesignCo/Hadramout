package com.hyperdesign.myapplication.presentation.profile.myorders.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.ReorderRequest
import com.hyperdesign.myapplication.domain.usecase.profile.ReOrderUseCase
import com.hyperdesign.myapplication.domain.usecase.profile.ShowMyOrdersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyOrderViewModel(
    private val reOrderUseCase: ReOrderUseCase,
    private val showMyOrdersUseCase: ShowMyOrdersUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    
    private var _myOrdersState = MutableStateFlow(MyOrdersModelState())
    val myOrdersState : StateFlow<MyOrdersModelState> = _myOrdersState.asStateFlow()
    
    fun handleIntents(intent: MyOrdersIntants){
        when(intent){
            is MyOrdersIntants.ReOrder -> {
                reOrder(intent.orderId)
            }

            is MyOrdersIntants.ShowMyOrders -> {
                showMyOrders(intent.type)
            }
        }
    }

    private fun showMyOrders(type: Int) {
        _myOrdersState.value=_myOrdersState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = showMyOrdersUseCase(type)
                _myOrdersState.value=_myOrdersState.value.copy(
                    isLoading = false,
                    MyOrdersResponse = response
                )


            }.onSuccess {
                _myOrdersState.value=_myOrdersState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _myOrdersState.value=_myOrdersState.value.copy(
                    isLoading = false,
                    errorMsg = it.message.toString()
                )
                Log.e("faild showMyOrders",it.message.toString())
            }
        }

    }

    private fun reOrder(orderId: String) {
        _myOrdersState.value=_myOrdersState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val reOrderRequest = ReorderRequest(orderId = orderId, areaId = tokenManager.getAreaId().toString())
                val response = reOrderUseCase(reOrderRequest)
                _myOrdersState.value=_myOrdersState.value.copy(
                    isLoading = false,
                    reOrderResponse = response
                )


            }.onSuccess {
                _myOrdersState.value=_myOrdersState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _myOrdersState.value=_myOrdersState.value.copy(
                    isLoading = false,
                    errorMsg = it.message.toString()
                )
                Log.e("faild reOrder",it.message.toString())
            }
        }
    }
}