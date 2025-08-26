package com.hyperdesign.myapplication.presentation.menu.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.usecase.menu.GetMealDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealDetailsViewModel(
    private val getMealDetailsUseCase: GetMealDetailsUseCase

): ViewModel() {

    private var _MealDetailsState = MutableStateFlow(MenuStateModel())
    val MealDetailsState: StateFlow<MenuStateModel> get() = _MealDetailsState

    fun handleIntents(intent: MealDetialsIntents) {
        when(intent){
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

}