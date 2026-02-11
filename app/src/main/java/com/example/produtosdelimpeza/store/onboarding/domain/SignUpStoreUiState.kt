package com.example.produtosdelimpeza.store.onboarding.domain


data class SignUpStoreUiState(
    val isLoading: Boolean = false,
    val goToHome: Boolean = false,
    val error: String? = null
)