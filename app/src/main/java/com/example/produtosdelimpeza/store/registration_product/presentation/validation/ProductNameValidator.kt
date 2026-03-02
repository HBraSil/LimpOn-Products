package com.example.produtosdelimpeza.store.registration_product.presentation.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object ProductNameValidator {
    fun isValid(name: String): UiText.StringResource? {
        if (name.isBlank()) return UiText.StringResource(R.string.error_empty_field)
        if (name.length < 2) return UiText.StringResource(R.string.product_name_short)


        return null
    }

}