package com.hyperdesign.myapplication.presentation.menu.mvi

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.AddOrderRequest
import com.hyperdesign.myapplication.domain.usecase.menu.AddMealToCartUseCase
import com.hyperdesign.myapplication.domain.usecase.menu.GetMealDetailsUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MealDetailsViewModel(
    private val getMealDetailsUseCase: GetMealDetailsUseCase,
    private val addMealToCartUseCase: AddMealToCartUseCase,
    val tokenManager: TokenManager,
    private @ApplicationContext val context: Context


): ViewModel() {


    private var _MealDetailsState = MutableStateFlow(MenuStateModel())
    val MealDetailsState: StateFlow<MenuStateModel> get() = _MealDetailsState

    var showAuthDialoge = mutableStateOf(false)

    fun handleIntents(intent: MealDetialsIntents) {
        when(intent){
            is MealDetialsIntents.changeSizeId->{
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    sizeId = intent.sizeId
                )
            }

            is MealDetialsIntents.changeChoices->{
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    choices = intent.choices
                )
            }

            is MealDetialsIntents.changeQuantity -> {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    quantity = intent.quantity
                )
            }
            is MealDetialsIntents.changeMealId -> {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    mealId = intent.mealId

                )
            }
            is MealDetialsIntents.changeBranchId -> {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    branchId = intent.branchId
                )

            }
            is MealDetialsIntents.showMealDetails -> {
                showMealDetails(
                    branchId = intent.branchId,
                    mealId = intent.mealId
                )

            }
            is MealDetialsIntents.changePickupStatus->{
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    pickUpStatus = intent.pickupStatus

                )
            }
            is MealDetialsIntents.addMealToCart -> {
                addMealIntoCart(AddOrderRequest(
                    branchId = intent.branchId,
                    mealId = intent.mealId,
                    sizeId = intent.sizeId,
                    quantity = intent.quantity,
                    choices = intent.choices,
                    pickupStatus = intent.pickupStatus,
                    areaId = tokenManager.getAreaId().toString(),
                    deviceId = if (tokenManager.getUserData()?.authenticated=="authenticated"){
                        ""
                    }else{
                        "android-${getAndroidId(context =context)}"
                    }


                ))
            }
        }

    }

    private fun addMealIntoCart(addOrderRequest: AddOrderRequest) {
        _MealDetailsState.value = _MealDetailsState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = addMealToCartUseCase(
                    addOrderRequest

                )
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    isLoading = false,
                    AddToCartData = response
                )
            }.onSuccess {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    isLoading = false
                )
            }.onFailure {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    isLoading = false,
                )
            }

        }
    }

    private fun showMealDetails(branchId: Int, mealId: Int) {
        _MealDetailsState.value = _MealDetailsState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getMealDetailsUseCase.invoke(branchId, mealId)
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    isLoading = false,
                    MealDetailsData = response
                )
            }.onSuccess {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    isLoading = false
                )
            }.onFailure {
                _MealDetailsState.value = _MealDetailsState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
            }
        }



    }

    fun getOrCreateDeviceId(): String {
        var deviceId = tokenManager.getDeviceId()

        if (deviceId.isNullOrEmpty()) {
            deviceId = UUID.randomUUID().toString()
           tokenManager.saveDeviceId(deviceId)
        }

        return deviceId
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

}