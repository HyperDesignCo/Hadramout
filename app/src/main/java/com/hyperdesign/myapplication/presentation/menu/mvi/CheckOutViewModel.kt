package com.hyperdesign.myapplication.presentation.menu.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.Entity.CheckOutRequest
import com.hyperdesign.myapplication.domain.usecase.cart.CheckOutUseCase
import com.hyperdesign.myapplication.domain.usecase.home.GetAllAddressUseCase
import com.hyperdesign.myapplication.presentation.di.viewModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckOutViewModel(
    private val getAllAddressUseCase: GetAllAddressUseCase,
    private val checkOutUseCase: CheckOutUseCase
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
                    checkOutResponse = response
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