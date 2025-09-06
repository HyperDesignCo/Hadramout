package com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.Entity.FinishOrderRequest
import com.hyperdesign.myapplication.domain.usecase.profile.DisplayAboutUsUseCase
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginStateModel
import com.hyperdesign.myapplication.presentation.main.mvi.RefreshTOkenIntents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WhoAreWeViewModel(
    private val aboutUsUseCase: DisplayAboutUsUseCase
): ViewModel(){

    private var _aboutUsState = MutableStateFlow(WhoAreWeModelState())
    val aboutUsState : StateFlow<WhoAreWeModelState> = _aboutUsState

     fun handelIntent(intent: WhoAreWeIntents){
        when(intent) {
            WhoAreWeIntents.DisplayAboutUsContent -> {
                displayAboutUs()
            }
        }

    }

    init {

        displayAboutUs()
    }

    private fun displayAboutUs() {
        _aboutUsState.value=_aboutUsState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = aboutUsUseCase()

                _aboutUsState.value=_aboutUsState.value.copy(
                    isLoading = false,
                    aboutUsResponse = response
                )


            }.onSuccess {
                _aboutUsState.value=_aboutUsState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _aboutUsState.value=_aboutUsState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("faild getAboutUsContent",it.message.toString())
            }
            
            }
    }

}