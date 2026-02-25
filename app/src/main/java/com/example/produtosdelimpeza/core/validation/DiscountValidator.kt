package com.example.produtosdelimpeza.core.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object DiscountValidator {
    fun isValid(discount: String): UiText.StringResource? {
        if (discount.isBlank()) return UiText.StringResource(R.string.error_discount_blank)

        if (discount.toDouble() > 100) return UiText.StringResource(R.string.error_discount_greater_than_100)


        return null
    }
}