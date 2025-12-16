package com.example.produtosdelimpeza.compose.auth.signup

data class SignUpUiState(
    val isLoading: Boolean = false,
    val goToSignUpCode: Boolean = false,
    val error: String? = null
)