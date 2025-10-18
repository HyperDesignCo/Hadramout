package com.hyperdesign.myapplication.presentation.profile.common.mvi

import com.hyperdesign.myapplication.domain.Entity.EditProfileResponseEntity

data class ProfileState(
    val loading: Boolean = false,
    val nameState: String = "",
    val emailState: String = "",
    val phoneNumber: String = "",
    val image: String? = null,
    val isUpdating: Boolean = false,
    val updateSuccess: Boolean = false,
    val errorMessage: String? = null
)