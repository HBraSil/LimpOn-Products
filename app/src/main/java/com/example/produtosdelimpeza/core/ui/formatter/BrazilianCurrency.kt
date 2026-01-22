package com.example.produtosdelimpeza.core.ui.formatter

import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.commons.ProfileModeKey
import java.util.Locale

fun Double.toBrazilianCurrency() = String.format(Locale("pt", "BR"), "%.2f", this)