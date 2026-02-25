package com.example.produtosdelimpeza.core.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object CouponCodeValidator {
    fun isValid(field: String): UiText.StringResource? {
        if (field.isEmpty()) return UiText.StringResource(R.string.coupon_code_empty)


        return null
    }

}
