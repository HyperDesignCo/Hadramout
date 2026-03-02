package com.hyperdesign.myapplication.presentation.profile.points.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.usecase.profile.GetProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PointsViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PointsState())
    val state: StateFlow<PointsState> = _state.asStateFlow()

    init {
        handleIntent(PointsContract.Intent.LoadProfile)
    }

    fun handleIntent(intent: PointsContract.Intent) {
        when (intent) {
            is PointsContract.Intent.LoadProfile -> loadProfile()
        }
    }

    private fun loadProfile() {
        _state.value = _state.value.copy(isLoading = true, errorMsg = "")
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                getProfileUseCase()
            }.onSuccess { response ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    profileData = response
                )
            }.onFailure {
                Log.e("PointsViewModel", "loadProfile failed: ${it.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMsg = it.message.toString()
                )
            }
        }
    }
}