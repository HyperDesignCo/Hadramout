package com.hyperdesign.myapplication.presentation.utilies

sealed class ValidationEvent {
    object Success : ValidationEvent()
    data class Failure(val errorMessage: String) : ValidationEvent()
}