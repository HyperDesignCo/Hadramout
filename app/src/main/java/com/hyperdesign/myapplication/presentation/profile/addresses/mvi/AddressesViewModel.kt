package com.hyperdesign.myapplication.presentation.profile.addresses.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.usecase.home.GetAllAddressUseCase
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutIntents
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutStateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressesViewModel(
    private val getAllAddressUseCase: GetAllAddressUseCase,

): ViewModel() {

    private var _addressState = MutableStateFlow(AddressesModelState())
    val addressState : StateFlow<AddressesModelState> =_addressState.asStateFlow()

    init {
        getAllAddress()
    }
    fun handleIntents(intent: AddressesIntents) {
        when (intent) {
            AddressesIntents.GetAddress -> {
                getAllAddress()

            }
        }
    }

    private fun getAllAddress() {
        _addressState.value=_addressState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getAllAddressUseCase()
                _addressState.value=_addressState.value.copy(
                    isLoading = false,
                    addresses = response
                )


            }.onSuccess {
                _addressState.value=_addressState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _addressState.value=_addressState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("faild to get address",it.message.toString())
            }
        }

    }



}