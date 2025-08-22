package com.hyperdesign.myapplication.presentation.auth.signup.mvi

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.RegisterRequst
import com.hyperdesign.myapplication.domain.usecase.auth.RegisterUseCase
import com.hyperdesign.myapplication.presentation.utilies.ValidatePhoneNumber
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

class RegisterViewModel(
    private val validateText: ValidateText,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val tokenManager: TokenManager,
    private val registerUseCase: RegisterUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var _state = MutableStateFlow(RegisterModelState())
    val state: StateFlow<RegisterModelState> = _state.asStateFlow()

    private var _validationEvent = Channel<ValidationEvent>()
    val validationEvent = _validationEvent.receiveAsFlow()

    fun onIntentEvent(intent: RegisterIntents) {
        when (intent) {
            is RegisterIntents.EmailChanged -> {
                _state.value = _state.value.copy(
                    email = intent.email,
                    emailError = null
                )
            }
            is RegisterIntents.PasswordChanged -> {
                _state.value = _state.value.copy(
                    password = intent.password,
                    passwordError = null
                )
            }
            is RegisterIntents.NameChanged -> {
                _state.value = _state.value.copy(
                    userName = intent.name,
                    userNameError = null
                )
            }
            is RegisterIntents.MobileChanged -> {
                _state.value = _state.value.copy(
                    phoneNumber = intent.mobile,
                    phoneNumberError = null
                )
            }
            is RegisterIntents.ConfirmPasswordChanged -> {
                _state.value = _state.value.copy(
                    confirmPassword = intent.confirmPassword,
                    confirmPasswordError = null
                )
            }
            is RegisterIntents.RegisterClicked -> submitData()
        }
    }

    private fun submitData() {
        val emailResult = validateText.execute(_state.value.email)
        val passwordResult = validateText.execute(_state.value.password)
        val nameResult = validateText.execute(_state.value.userName)
        val mobileResult = validatePhoneNumber.execute(_state.value.phoneNumber)
        val confirmPasswordResult = validateText.execute(_state.value.confirmPassword)

        val hasError = listOf(emailResult, passwordResult, nameResult, mobileResult, confirmPasswordResult).any { !it.successful }

        val passwordMatchError = if (_state.value.password != _state.value.confirmPassword && _state.value.confirmPassword.isNotEmpty()) {
            context.getString(R.string.confirm_password_is_should_be_same_as_password)
        } else {
            null
        }

        val emailError = if (emailResult.successful) null else context.getString(R.string.enter_email_address)
        val passwordError = if (passwordResult.successful) null else context.getString(R.string.enter_your_password)
        val userNameError = if (nameResult.successful) null else context.getString(R.string.enter_your_name)
        val phoneNumberError = if (mobileResult.successful) null else context.getString(R.string.invalid_phone_number_format)
        val confirmPasswordError = if (confirmPasswordResult.successful) passwordMatchError else context.getString(R.string.enter_your_confirm_password)

        if (hasError || passwordMatchError != null) {
            _state.value = _state.value.copy(
                emailError = emailError,
                passwordError = passwordError,
                userNameError = userNameError,
                phoneNumberError = phoneNumberError,
                confirmPasswordError = confirmPasswordError,
                isLoading = false
            )
            return
        }

        // Set loading state on the main thread
        _state.value = _state.value.copy(
            isLoading = true,
            emailError = null,
            passwordError = null,
            userNameError = null,
            phoneNumberError = null,
            confirmPasswordError = null,
            errorMessage = null
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = registerUseCase.invoke(
                    RegisterRequst(
                        email = _state.value.email,
                        password = _state.value.password,
                        name = _state.value.userName,
                        mobile = _state.value.phoneNumber,
                        device_token = ""
                    )
                )
                // Update state back on the main thread
                _state.value = _state.value.copy(
                    isLoading = false,
                    registerRespnse = response
                )
                when (response.message) {
                    "register success" -> {
                        _validationEvent.send(ValidationEvent.Success)
                        tokenManager.saveAccessToken(response.accessToken)
                        tokenManager.saveUserData(response.user)
                    }
                    "email is used" -> _validationEvent.send(ValidationEvent.Failure(errorMessage = context.getString(R.string.email_is_already_used)))
                    else -> _validationEvent.send(ValidationEvent.Failure(errorMessage = context.getString(R.string.email_is_already_used)))
                }
            } catch (e: Exception) {
                // Update state back on the main thread
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
                _validationEvent.send(ValidationEvent.Failure(errorMessage = e.message.toString()))
            }
        }
    }
}