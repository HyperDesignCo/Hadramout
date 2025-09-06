package com.hyperdesign.myapplication.presentation.profile.settings.common.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.usecase.profile.GetLagugaueUsecase // Fixed typo
import com.hyperdesign.myapplication.domain.usecase.profile.SetLagugaueUsecase // Fixed typo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingViewModel(
    private val setLanguageUseCase: SetLagugaueUsecase,
    private val getLanguageUseCase: GetLagugaueUsecase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private var _settingsState = MutableStateFlow(SettingModelState())
    val settingsState: StateFlow<SettingModelState> = _settingsState

    fun handelIntent(intent: SettingIntents): String? {
        return when (intent) {
            is SettingIntents.OnSelectedLanguage -> {
                _settingsState.value = _settingsState.value.copy(
                    selectedLanguage = intent.language
                )
                intent.language // Return the selected language
            }
            is SettingIntents.SetSelectLanguage -> {
                setLanguage(intent.language)
                intent.language // Return the language being set
            }
            is SettingIntents.GetLanguage -> {
                getLanguage() // Return the retrieved language
            }
        }
    }


    fun logOut(){
        tokenManager.clearAll()
    }
     fun setLanguage(language: String) {
        viewModelScope.launch {
            setLanguageUseCase(language)
            _settingsState.value = _settingsState.value.copy(selectedLanguage = language)
        }
    }

     fun getLanguage(): String {
        return getLanguageUseCase() ?: "en" // Default to "en" if null
    }
}