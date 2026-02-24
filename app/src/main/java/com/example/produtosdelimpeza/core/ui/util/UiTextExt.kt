package com.example.produtosdelimpeza.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.produtosdelimpeza.core.presentation.UiText

// UiTextExt.kt
@Composable
fun UiText.asString(): String {
    return when(this) {
        is UiText.DynamicString -> value
        is UiText.StringResource -> stringResource(resId, *args)
    }
}