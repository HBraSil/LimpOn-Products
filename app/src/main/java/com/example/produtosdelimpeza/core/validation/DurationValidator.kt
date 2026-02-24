package com.example.produtosdelimpeza.core.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object DurationValidator {
    fun isValid(duration: Int?): UiText.StringResource? {
        if (duration == null) return UiText.StringResource(R.string.error_duration_blank)

        if (duration < 1) return UiText.StringResource(R.string.error_duration_less_than_one)

        return null
    }
}
