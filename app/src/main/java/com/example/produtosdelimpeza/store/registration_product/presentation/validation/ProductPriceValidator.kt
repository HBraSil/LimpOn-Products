package com.example.produtosdelimpeza.store.registration_product.presentation.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object ProductPriceValidator {
    fun isValid(price: String): UiText.StringResource? {
        if (price.toDouble() <= 0) return UiText.StringResource(R.string.product_price_invalid)

        return null
    }
}