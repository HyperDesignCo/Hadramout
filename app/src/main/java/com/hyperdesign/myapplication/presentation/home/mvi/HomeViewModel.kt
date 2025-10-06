package com.hyperdesign.myapplication.presentation.home.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.usecase.home.GetBranchesUseCase
import com.hyperdesign.myapplication.domain.usecase.home.GetHomeMenueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getBranchesUseCase: GetBranchesUseCase,
    private val getHomeMenuByIdUseCase: GetHomeMenueUseCase,
    val tokenManager: TokenManager
) : ViewModel() {
    private var _homeState = MutableStateFlow(HomeStateModel())
    val homeState: StateFlow<HomeStateModel> = _homeState.asStateFlow()

    fun handleIntents(intent: HomeIntents) {
        when (intent) {
            is HomeIntents.GetBranches -> {
                getBranches()
            }
            is HomeIntents.changeBranchId -> {
                _homeState.value = _homeState.value.copy(
                    branchId = intent.id
                )
                getHomeMenuById()
            }
            is HomeIntents.GetHomeMenuId -> {
                getHomeMenuById()
            }
        }
    }

    init {
        getBranches() // Only fetch branches initially
    }

    private fun getHomeMenuById() {
        if (_homeState.value.branchId == 0) {
            Log.d("HomeViewModel", "Skipping getHomeMenuById: branchId is 0")
            return // Avoid fetching with invalid branchId
        }
        _homeState.value = _homeState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getHomeMenuByIdUseCase.invoke(_homeState.value.branchId)
                Log.d("HomeViewModel", "Raw HomeResponse: $response")
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    homeMenues = response
                )
            }.onSuccess {
                _homeState.value = _homeState.value.copy(isLoading = false)
                Log.d("HomeViewModel", "getHomeMenuSuccess: ${_homeState.value.homeMenues}")
            }.onFailure {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
                Log.d("HomeViewModel", "getHomeMenuFailure: ${_homeState.value.errorMessage}")
            }
        }
    }

    private fun getBranches() {
        _homeState.value = _homeState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getBranchesUseCase.invoke()
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    branches = response,
                    branchId = response.branches.firstOrNull()?.id ?: 0 // Set default branchId
                )
            }.onSuccess {
                Log.d("HomeViewModel", "getBranchesSuccess: ${_homeState.value.branches}")
                if (_homeState.value.branchId != 0) {
                    getHomeMenuById() // Fetch menu for the first branch
                }
            }.onFailure {
                _homeState.value = _homeState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
                Log.d("HomeViewModel", "getBranchesFailure: ${_homeState.value.errorMessage}")
            }
        }
    }
}