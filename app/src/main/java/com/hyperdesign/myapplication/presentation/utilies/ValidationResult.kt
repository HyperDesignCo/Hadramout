package com.hyperdesign.myapplication.presentation.utilies

data class ValidationResult
(
    val successful: Boolean,
    val errorMessage: String? = null

)