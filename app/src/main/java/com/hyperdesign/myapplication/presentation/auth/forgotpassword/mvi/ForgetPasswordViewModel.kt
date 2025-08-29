package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.ForgetPasswordRequest
import com.hyperdesign.myapplication.domain.usecase.auth.ForgetPasswordUseCase
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val validateText: ValidateText,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordModelState())
    val forgetPasswordState: StateFlow<ForgetPasswordModelState> = _forgetPasswordState.asStateFlow()

    private var _forgetPasswordChannel = Channel<ValidationEvent>()
    val forgetPasswordChannel = _forgetPasswordChannel.receiveAsFlow()

    fun handleIntents(intent: ForgetPasswordIntents) {
        when (intent) {
            is ForgetPasswordIntents.OnEmailChanged -> {
                _forgetPasswordState.value = _forgetPasswordState.value.copy(
                    email = intent.email,
                    emailError = null
                )
            }
            is ForgetPasswordIntents.PasswordForgetClickAction -> {
                forgetPassword()
            }

        }


}

    private fun forgetPassword() {
        val emailResult = validateText.execute(_forgetPasswordState.value.email)

        val hasError = listOf(emailResult).any { !it.successful }

        val emailError = if (emailResult.successful) null else context.getString(R.string.email_is_required)

        if (hasError) {
            _forgetPasswordState.value = _forgetPasswordState.value.copy(
                emailError = emailError,
                isLoading = false
            )
            return
        }

        _forgetPasswordState.value = _forgetPasswordState.value.copy(
            isLoading = true,
            emailError = null,
            errorMessage = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val forgetPasswordRequest =
                    ForgetPasswordRequest(email = _forgetPasswordState.value.email)
                val response = forgetPasswordUseCase.invoke(forgetPasswordRequest)

                _forgetPasswordState.value = _forgetPasswordState.value.copy(
                    isLoading = false,
                    emailError = null,
                    errorMessage = null,
                    ForgetPsasswordResponse = response
                )
                when (response.message) {
                    "Check Your email please now."->{
                        _forgetPasswordChannel.send(ValidationEvent.Success)
                    }
                    else -> {
                        _forgetPasswordChannel.send(ValidationEvent.Failure(response.message))
                    }
                }

            }.onSuccess {

                _forgetPasswordState.value = _forgetPasswordState.value.copy(
                    isLoading = false,
                    emailError = null,
                    errorMessage = null
                )
            }.onFailure {
                _forgetPasswordState.value = _forgetPasswordState.value.copy(
                    isLoading = false,
                    emailError = null,
                    errorMessage = it.message
                )
                _forgetPasswordChannel.send(ValidationEvent.Failure(it.message ?: "Error"))

            }
        }

    }
}
