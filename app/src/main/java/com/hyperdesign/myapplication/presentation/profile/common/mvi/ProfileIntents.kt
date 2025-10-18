package com.hyperdesign.myapplication.presentation.profile.common.mvi

sealed class ProfileIntents {

    data class OnChangeName(val name: String) : ProfileIntents()
    data class OnChangeEmail(val email: String) : ProfileIntents()
    data class OnChangePhone(val phone: String) : ProfileIntents()

    data class OnChangeImage(val image: String) : ProfileIntents()

    object UpdateDataClick : ProfileIntents()

}