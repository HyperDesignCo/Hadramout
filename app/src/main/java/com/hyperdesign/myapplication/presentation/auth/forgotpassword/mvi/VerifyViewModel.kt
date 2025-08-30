package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class VerifyViewModel() : ViewModel() {

    private val _state = MutableStateFlow(VerifyModelState())
    val state = _state.asStateFlow()

    private val _effects = Channel<ValidationEvent>()
    val effects = _effects.receiveAsFlow()

    private var _code = mutableStateOf("")
    val code =_code

    private val backendCode = _code

    fun processIntent(intent: VerifyIntent) {
        when (intent) {
            is VerifyIntent.UpdateDigit -> {
                val updatedDigits = _state.value.codeDigits.toMutableList()
                updatedDigits[intent.index] = intent.value.take(1) // Ensure single digit
                _state.value = _state.value.copy(codeDigits = updatedDigits)
            }
            VerifyIntent.SubmitCode -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    val enteredCode = _state.value.codeDigits.joinToString("")
                    if (enteredCode == backendCode.value && enteredCode.length == 5) {
                        _state.value = _state.value.copy(isVerified = true, isLoading = false)
                        _effects.send(ValidationEvent.Success)
                    } else {
                        _state.value = _state.value.copy(isLoading = false, error = "Invalid code")
                        _effects.send(ValidationEvent.Failure("Invalid verification code"))
                    }
                }
            }
            VerifyIntent.ResendCode -> {
                // TODO: Implement resend logic, e.g., API call to resend code
                // For now, just simulate
                viewModelScope.launch {
                    _effects.send(ValidationEvent.Failure("Code resent (simulated)"))
                }
            }
            VerifyIntent.BackPressed -> {
                // Handled in UI
            }
        }
    }
}