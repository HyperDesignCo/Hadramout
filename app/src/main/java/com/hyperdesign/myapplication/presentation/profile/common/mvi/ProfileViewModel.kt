package com.hyperdesign.myapplication.presentation.profile.common.mvi

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperdesign.myapplication.data.local.TokenManager
import com.hyperdesign.myapplication.domain.Entity.EditProfileRequest
import com.hyperdesign.myapplication.domain.Entity.UserEntity
import com.hyperdesign.myapplication.domain.usecase.profile.EditProfileUseCase
import com.hyperdesign.myapplication.presentation.utilies.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val editProfileUseCase: EditProfileUseCase,
    val tokenManager: TokenManager
) : ViewModel() {

    private var _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private var _channelEvent = Channel<ValidationEvent>()
    val channelEvent = _channelEvent

    var showAuthDialoge = mutableStateOf(false)

    init {
        fetchUserData()
    }

    fun handleIntents(intent: ProfileIntents) {
        when (intent) {
            is ProfileIntents.OnChangeEmail -> {
                _profileState.value = _profileState.value.copy(
                    emailState = intent.email
                )
            }
            is ProfileIntents.OnChangeImage -> {
                _profileState.value = _profileState.value.copy(
                    image = intent.image
                )
            }
            is ProfileIntents.OnChangeName -> {
                _profileState.value = _profileState.value.copy(
                    nameState = intent.name
                )
            }
            is ProfileIntents.OnChangePhone -> {
                _profileState.value = _profileState.value.copy(
                    phoneNumber = intent.phone
                )
            }
            is ProfileIntents.UpdateDataClick -> {
                updateData()
            }
        }
    }

    private fun fetchUserData() {
        _profileState.value = _profileState.value.copy(
            loading = false,
            nameState = tokenManager.getUserData()?.name ?: "",
            emailState = tokenManager.getUserData()?.email ?: "",
            phoneNumber = tokenManager.getUserData()?.mobile ?: "",
            image = tokenManager.getUserData()?.image ?: ""
        )
    }

    private fun updateData() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(
                isUpdating = true,
                errorMessage = null,
                updateSuccess = false
            )

            try {
                val request = EditProfileRequest(
                    name = _profileState.value.nameState,
                    email = _profileState.value.emailState,
                    mobile = _profileState.value.phoneNumber,
                    image = _profileState.value.image // This will be the image URI or null
                )

                val response = editProfileUseCase.invoke(request)

                // Update token manager with new user data
                val user = UserEntity(
                    id = response.user.id,
                    name = response.user.name,
                    email = response.user.email,
                    mobile = response.user.mobile,
                    image = response.user.image,
                    balance = response.user.balance,
                    authenticated = "authenticated"

                )
                tokenManager.saveUserData(user)

                // Update UI state
                _profileState.value = _profileState.value.copy(
                    isUpdating = false,
                    updateSuccess = true,
                    nameState = response.user.name,
                    emailState = response.user.email,
                    phoneNumber = response.user.mobile,
                    image = response.user.image
                )

                Log.d("ProfileViewModel", "Profile updated successfully: ${response.message}")

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Profile update failed: ${e.message}")
                _profileState.value = _profileState.value.copy(
                    isUpdating = false,
                    errorMessage = e.message ?: "Failed to update profile"
                )
            }
        }
    }

    fun resetUpdateState() {
        _profileState.value = _profileState.value.copy(
            updateSuccess = false,
            errorMessage = null
        )
    }
}