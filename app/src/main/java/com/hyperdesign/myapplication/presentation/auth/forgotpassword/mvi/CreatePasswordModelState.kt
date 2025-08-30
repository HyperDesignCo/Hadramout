package com.hyperdesign.myapplication.presentation.auth.forgotpassword.mvi

import com.hyperdesign.myapplication.domain.Entity.LoginEntity
import com.hyperdesign.myapplication.domain.Entity.LoginRequest

data class CreatePasswordModelState(
    val isLoading: Boolean =false,
    val errorMsg :String ="",
    val newPassword:String ="",
    val newPasswordError :String? =null,
    val createPasswordState : LoginEntity? =null
)
