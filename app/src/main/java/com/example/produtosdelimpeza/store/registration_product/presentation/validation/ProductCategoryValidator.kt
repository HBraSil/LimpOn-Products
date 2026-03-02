package com.example.produtosdelimpeza.store.registration_product.presentation.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object ProductCategoryValidator {
    fun isValid(category: String): UiText.StringResource? {
        if (category.isBlank()) return UiText.StringResource(R.string.error_empty_field)


        return null
    }
}
