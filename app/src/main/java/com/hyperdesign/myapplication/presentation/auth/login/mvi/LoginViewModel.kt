package com.hyperdesign.myapplication.presentation.auth.login.mvi

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.LoginRequest
import com.hyperdesign.myapplication.domain.usecase.auth.LoginUseCase
import com.hyperdesign.myapplication.presentation.utilies.ValidateText
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateText: ValidateText,
    val tokenManager: TokenManager,
    @ApplicationContext private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _state = MutableStateFlow(LoginStateModel())
    val state: StateFlow<LoginStateModel> = _state.asStateFlow()

    private val _validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = _validationEventChannel.receiveAsFlow()

    fun onIntent(intent: LoginIntents) {
        when (intent) {
            is LoginIntents.PhoneNumberChanged -> {
                _state.value = _state.value.copy(
                    phoneNumber = intent.phone,
                    phoneNumberError = null
                )
            }
            is LoginIntents.PasswordChanged -> {
                _state.value = _state.value.copy(
                    password = intent.password,
                    passwordError = null
                )
            }
            is LoginIntents.LoginEvent -> {
                // Use parameters from LoginEvent to set state
                _state.value = _state.value.copy(
                    phoneNumber = intent.phone,
                    password = intent.pass,
                    phoneNumberError = null,
                    passwordError = null
                )
                submitData()
            }
        }
    }

    private fun submitData() {
        val phoneResult = validateText.execute(_state.value.phoneNumber)
        val passwordResult = validateText.execute(_state.value.password)

        val hasError = listOf(phoneResult, passwordResult).any { !it.successful }

        val phoneError = if (phoneResult.successful) null else context.getString(R.string.please_enter_your_email_or_phone_number)
        val passwordError = if (passwordResult.successful) null else context.getString(R.string.please_enter_your_password)

        if (hasError) {
            _state.value = _state.value.copy(
                phoneNumberError = phoneError,
                passwordError = passwordError,
                isLoading = false
            )
            viewModelScope.launch(dispatcher) {
                _validationEventChannel.send(ValidationEvent.Failure("Invalid input"))
            }
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            phoneNumberError = null,
            passwordError = null,
            errorMessage = null
        )

        viewModelScope.launch(dispatcher) {
            try {
                val response = loginUseCase.invoke(
                    LoginRequest(
                        email = _state.value.phoneNumber,
                        password = _state.value.password,
                        deviceToken = ""
                    )
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    loginResponse = response
                )

                when (response.message) {
                    "login success" -> {
                        _validationEventChannel.send(ValidationEvent.Success)
                        tokenManager.saveAccessToken(response.accessToken)
                        tokenManager.saveUserData(response.user)
                    }
                    else -> {
                        _state.value = _state.value.copy(
                            errorMessage = "Request failed: ${response.message}"
                        )
                        _validationEventChannel.send(ValidationEvent.Failure("Request failed: ${response.message}"))
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Request failed: ${e.message}"
                )
                _validationEventChannel.send(ValidationEvent.Failure("Request failed: ${e.message}"))
            }
        }
    }
}