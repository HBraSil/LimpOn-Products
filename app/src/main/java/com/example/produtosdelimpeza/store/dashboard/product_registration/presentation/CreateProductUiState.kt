package com.example.produtosdelimpeza.store.dashboard.product_registration.presentation

data class CreateProductUiState(
    val isLoading: Boolean = false,
    val showSessionExpired: Boolean = false,
    val showNoInternet: Boolean = false,
    val unknwonError: Boolean = false,
    val success: Boolean = false
)