package com.hyperdesign.myapplication.presentation.profile.settings.common.mvi

sealed class StaticPagesIntents {

    data class DisplayPage(val pageNum:Int): StaticPagesIntents()
}