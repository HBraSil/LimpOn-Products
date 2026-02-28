package com.example.produtosdelimpeza.store.registration_product.presentation.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object ProductDescriptionValidator {
    fun isValid(description: String): UiText.StringResource? {
        if (description.isBlank()) return UiText.StringResource(R.string.product_description_empty)

        return null
    }
}
