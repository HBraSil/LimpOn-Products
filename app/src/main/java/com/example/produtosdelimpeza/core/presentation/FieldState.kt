package com.example.produtosdelimpeza.core.presentation



data class FieldState(
    val field: String = "",
    val error: UiText? = null,
    val isValid: Boolean = false
)