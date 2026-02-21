package com.example.produtosdelimpeza.core.auth.presentation

data class AuthUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)