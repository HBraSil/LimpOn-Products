package com.example.produtosdelimpeza.core.presentation


data class SessionUserErrors(
    val showSessionExpired: Boolean = false,
    val showNoInternet: Boolean = false,
    val unknwonError: Boolean = false
)
