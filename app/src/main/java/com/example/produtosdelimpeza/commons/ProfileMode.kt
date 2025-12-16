package com.example.produtosdelimpeza.commons


enum class ProfileMode(val mode: String) {
    CUSTOMER("customer"),
    STORE("store"),
    UNKNOWN("unknown")
}
// Sua data class refatorada
data class LastUserMode(
    val currentMode: String? = null
)