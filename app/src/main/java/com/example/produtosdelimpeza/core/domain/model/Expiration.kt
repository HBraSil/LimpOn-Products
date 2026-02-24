package com.example.produtosdelimpeza.core.domain.model

enum class Expiration(val day: Int?) {
    DAYS_7(7),
    DAYS_15(15),
    DAYS_30(30),
    CUSTOM(null),
    NONE(null)
}
