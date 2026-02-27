package com.example.produtosdelimpeza.store.registration_coupon.presentation

data class CreateCouponUiState(
    val isLoading: Boolean = false,
    val showSessionExpired: Boolean = false,
    val showNoInternet: Boolean = false,
    val unknwonError: Boolean = false,
    val success: Boolean = false
)