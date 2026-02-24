package com.example.produtosdelimpeza.core.validation

import android.util.Patterns
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object EmailValidator {
    fun validate(email: String): UiText.StringResource? {
        if (email.isBlank()) {
            return UiText.StringResource(R.string.error_email_blank)
        }

        if (!isEmailValid(email)) {
            return UiText.StringResource(R.string.error_email_invalid)
        }

        return null
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}