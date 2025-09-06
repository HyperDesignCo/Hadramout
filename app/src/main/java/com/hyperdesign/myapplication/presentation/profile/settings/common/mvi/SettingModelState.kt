package com.hyperdesign.myapplication.presentation.profile.settings.common.mvi

data class SettingModelState(
    val isLoading : Boolean =false,
    val errorMsg :String?=null,
    val selectedLanguage :String ="en"
)
