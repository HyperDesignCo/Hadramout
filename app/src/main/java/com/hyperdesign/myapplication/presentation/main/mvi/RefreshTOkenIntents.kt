package com.hyperdesign.myapplication.presentation.main.mvi

sealed class RefreshTOkenIntents {

    data class RefreshToken(val email: String) : RefreshTOkenIntents()
}