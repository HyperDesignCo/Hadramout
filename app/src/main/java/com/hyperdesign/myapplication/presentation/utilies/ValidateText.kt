package com.hyperdesign.myapplication.presentation.utilies

import android.content.Context
import com.hyperdesign.myapplication.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ValidateText (
    @ApplicationContext private val contexts: Context
) {
    fun execute(text: String): ValidationResult {
        if(
            text.isBlank())
        {
            return ValidationResult(
                successful = false,
                errorMessage = contexts.getString(R.string.this_field_is_required)
            )
        }

        return ValidationResult(successful = true)

    }
}

