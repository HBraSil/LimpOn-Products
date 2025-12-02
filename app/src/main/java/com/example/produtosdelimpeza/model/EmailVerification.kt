package com.example.produtosdelimpeza.model

sealed class EmailVerificationUiState {
    object Idle : EmailVerificationUiState()
    object Loading : EmailVerificationUiState()
    object Verified : EmailVerificationUiState()
    data class Error(val message: String?) : EmailVerificationUiState()
}
