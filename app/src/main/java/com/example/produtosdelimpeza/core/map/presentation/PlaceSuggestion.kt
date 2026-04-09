package com.example.produtosdelimpeza.core.map.presentation

import android.text.Spannable

data class PlaceSuggestion(
    val placeId: String,
    val primaryText: Spannable,
    val secondaryText: Spannable,
    val etaMinutes: String = "",
    val distance: String? = null
)