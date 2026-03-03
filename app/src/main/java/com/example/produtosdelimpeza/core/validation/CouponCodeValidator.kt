package com.example.produtosdelimpeza.core.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object CouponCodeValidator {
    fun isValid(field: String): UiText.StringResource? {
        if (field.isBlank()) return UiText.StringResource(R.string.error_empty_field)

        if (field.any { it.isWhitespace() }) return UiText.StringResource(R.string.error_white_spaces)


        return null
    }
}
