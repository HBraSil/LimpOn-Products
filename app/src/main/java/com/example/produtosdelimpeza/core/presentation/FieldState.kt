package com.example.produtosdelimpeza.core.presentation

data class FieldState(
    val field: String = "",
    val error: String? = null,
    val isValid: Boolean = false
)