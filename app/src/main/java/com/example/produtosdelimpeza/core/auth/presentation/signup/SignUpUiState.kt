package com.example.produtosdelimpeza.core.auth.presentation.signup

data class SignUpUiState(
    val isLoading: Boolean = false,
    val goToSignUpCode: Boolean = false,
    val error: String? = null
)