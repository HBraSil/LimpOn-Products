package com.example.produtosdelimpeza.model

data class SignUpUiState(
    val isLoading: Boolean = false,
    val goToSignUpCode: Boolean = false,
    val error: String? = null
)