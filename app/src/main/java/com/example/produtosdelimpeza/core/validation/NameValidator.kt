package com.example.produtosdelimpeza.core.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object NameValidator {
    fun isNameValid(name: String): UiText.StringResource? {
        if (name.isBlank()) {
            return UiText.StringResource(R.string.error_empty_field)
        }

        if (name.length < 3) {
            return UiText.StringResource(R.string.error_name_invalid)
        }

        return null
    }

    fun isLastNameValid(lastName: String): UiText.StringResource? {
        if (lastName.isBlank()) {
            return UiText.StringResource(R.string.error_empty_field)
        }


        return null
    }
}