package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

data class CreatePromotionUiState(
    val isLoading: Boolean = false,
    val showSessionExpired: Boolean = false,
    val showNoInternet: Boolean = false,
    val unknwonError: Boolean = false,
    val success: Boolean = false
)
