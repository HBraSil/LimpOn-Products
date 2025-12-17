package com.example.produtosdelimpeza.commons


enum class ProfileMode(val mode: String) {
    CUSTOMER("customer"),
    STORE("store"),
    NONE("none")
}
// Sua data class refatorada
data class LastUserMode(
    val currentMode: String? = null
)