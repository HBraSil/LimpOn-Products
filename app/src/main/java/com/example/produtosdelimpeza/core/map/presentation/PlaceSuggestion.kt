package com.example.produtosdelimpeza.core.map.presentation

import android.text.Spannable

data class PlaceSuggestion(
    val placeId: String = "",
    val primaryText: Spannable = Spannable.Factory.getInstance().newSpannable(""),
    val secondaryText: Spannable = Spannable.Factory.getInstance().newSpannable(""),
)
