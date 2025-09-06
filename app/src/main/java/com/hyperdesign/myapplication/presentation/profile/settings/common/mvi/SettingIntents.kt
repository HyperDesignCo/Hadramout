package com.hyperdesign.myapplication.presentation.profile.settings.common.mvi

sealed class SettingIntents {

    data class OnSelectedLanguage(val language:String): SettingIntents()

    data class SetSelectLanguage(val language:String): SettingIntents()

    data object GetLanguage : SettingIntents()
}