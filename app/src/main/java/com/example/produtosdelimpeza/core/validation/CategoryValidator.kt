package com.example.produtosdelimpeza.core.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object CategoryValidator {
    fun isValid(category: String): UiText.StringResource? {
        if (category.isBlank()) return UiText.StringResource(R.string.error_category_blank)

        return null
    }
}
