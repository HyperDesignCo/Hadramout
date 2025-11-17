package com.hyperdesign.myapplication.presentation.menu.mvi

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.usecase.menu.GetMenuUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
     val getMenusUseCase: GetMenuUseCase,
     val tokenManager: TokenManager
): ViewModel() {

    private var _menuState = MutableStateFlow(MenuStateModel())
    val menuState : StateFlow<MenuStateModel> = _menuState

    var showAuthDialoge = mutableStateOf(false)

    fun handleIntent(intent: MenuIntents){
        when(intent){
            is MenuIntents.getMenus -> {
                getMenu()
            }
            is MenuIntents.changeBranchId -> {
                _menuState.value = _menuState.value.copy(
                    branchId = intent.branchId
                )
                getMenu()
            }
        }

    }

    private fun getMenu() {
        _menuState.value = _menuState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = getMenusUseCase(tokenManager.getBranchId()?:0,tokenManager.getStatus())
                _menuState.value = _menuState.value.copy(
                    isLoading = false,
                    menuData = response
                )
            }.onSuccess {
                _menuState.value = _menuState.value.copy(isLoading = false)
            }.onFailure {
                _menuState.value = _menuState.value.copy(
                    isLoading = false,
                    errorMessage = it.message.toString()
                )
            }
        }
    }
}