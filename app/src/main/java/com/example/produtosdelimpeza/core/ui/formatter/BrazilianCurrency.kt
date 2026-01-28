package com.example.produtosdelimpeza.core.ui.formatter

import java.util.Locale

fun Double.toBrazilianCurrency() = String.format(Locale("pt", "BR"), "%.2f", this)