package com.hyperdesign.myapplication.presentation.profile.settings.common.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.domain.usecase.profile.ShowStaticPageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StaticPagesViewModel(
    private val staticPageUseCase: ShowStaticPageUseCase
): ViewModel() {

    private var _staticPageState = MutableStateFlow(StaticPagesModelState())
    val staticPageState: StateFlow<StaticPagesModelState> = _staticPageState.asStateFlow()
    
    fun handleIntents(intent: StaticPagesIntents){
        when(intent){
            is StaticPagesIntents.DisplayPage -> {
                dispalyStaticPage(intent.pageNum)
            }
        }
    }

    private fun dispalyStaticPage(pageNum: Int) {
        _staticPageState.value=_staticPageState.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val response = staticPageUseCase(pageNum)

                _staticPageState.value=_staticPageState.value.copy(
                    isLoading = false,
                    pagesResponse = response
                )


            }.onSuccess {
                _staticPageState.value=_staticPageState.value.copy(
                    isLoading = false,

                    )

            }.onFailure {
                _staticPageState.value=_staticPageState.value.copy(
                    isLoading = false,
                    errorMsg = it.message
                )
                Log.e("faild dispalyStaticPage",it.message.toString())
            }

        }
        
        
    }
}