package com.hyperdesign.myapplication.presentation.utilies

import android.content.Context
import com.hyperdesign.myapplication.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidatePhoneNumber (
    @ApplicationContext private val context: Context
){
    fun execute(phone: String): ValidationResult {
        if(
            phone.isBlank())
        {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.this_field_is_required)
            )
        }
        val regex = Regex("^(010|011|012|015)\\d{8,9}$")

        if (!regex.matches(phone)) {
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.invalid_phone_number_format)
            )
        }

        return ValidationResult(successful = true)

    }
}