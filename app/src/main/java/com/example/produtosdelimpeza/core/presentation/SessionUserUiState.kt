package com.example.produtosdelimpeza.core.presentation


data class SessionUserUiState(
    val showSessionExpiredDialog: Boolean = false,
    val showNoInternetToast: Boolean = false
)
