package com.example.produtosdelimpeza.store.registration_promotion.presentation

data class CreatePromotionUiState(
    val isLoading: Boolean = false,
    val showSessionExpired: Boolean = false,
    val showNoInternet: Boolean = false,
    val unknwonError: Boolean = false,
    val success: Boolean = false
)
