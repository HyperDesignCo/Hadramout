package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CreatePasswordRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.NewPasswordRequest
import com.hyperdesign.myapplication.domain.usecase.auth.CreateNewPasswordUseCase
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

class CreatePasswordViewModel(
    private val createNewPasswordUseCase: CreateNewPasswordUseCase,
    private val validateText: ValidateText,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _cratePasswordState = MutableStateFlow(CreatePasswordModelState())
    val createPasswordState: StateFlow<CreatePasswordModelState> = _cratePasswordState.asStateFlow()

    private var _createPasswordChannel = Channel<ValidationEvent>()
    val createPasswordChannel = _createPasswordChannel.receiveAsFlow()

    private var _email = mutableStateOf("")
    val email = _email

    fun handleIntent(intent: CreatePasswordIntents){
        when(intent){
            is CreatePasswordIntents.onChangeNewPassword -> {
                _cratePasswordState.value = _cratePasswordState.value.copy(
                    newPassword = intent.newPassword,
                    newPasswordError = null
                )
            }


            is CreatePasswordIntents.changePasswordClick->{
                createNewPassword()
            }
        }

    }

    private fun createNewPassword() {
        val newPassResult = validateText.execute(_cratePasswordState.value.newPassword)

        val hasError = listOf(newPassResult).any { !it.successful }

        val newPassError = if (newPassResult.successful) null else context.getString(R.string.new_password_is_required)

        if (hasError){
            _cratePasswordState.value.copy(
                isLoading = false,
                newPasswordError = newPassError
            )
            return
        }

        _cratePasswordState.value=_cratePasswordState.value.copy(
            isLoading = true,

        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val createPasswordReqest = NewPasswordRequest(
                    newPassword = _cratePasswordState.value.newPassword,
                    email = _email.value,
                    deviceToken = ""

                )
                val response = createNewPasswordUseCase.invoke(createPasswordReqest)

                _cratePasswordState.value=_cratePasswordState.value.copy(
                    isLoading = false,
                    createPasswordState =response

                    )


                if (response.message=="password updated successfully"){
                    _createPasswordChannel.send(ValidationEvent.Success)
                }


            }.onSuccess {

                _cratePasswordState.value=_cratePasswordState.value.copy(
                    isLoading = false,
                )



            }.onFailure {

                _createPasswordChannel.send(ValidationEvent.Failure(it.message.toString()))

                _cratePasswordState.value=_cratePasswordState.value.copy(
                    isLoading = false,
                )
            }
        }





    }


}