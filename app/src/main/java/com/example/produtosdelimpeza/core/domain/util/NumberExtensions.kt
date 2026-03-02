package com.example.produtosdelimpeza.core.domain.util

fun String.toCurrencyDouble(): Double {
    val digits = this.replace(Regex("\\D"), "")

    if (digits.isEmpty()) return 0.0

    return digits.toDouble() / 100
}
