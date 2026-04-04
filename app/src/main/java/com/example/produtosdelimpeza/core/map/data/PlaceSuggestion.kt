package com.example.produtosdelimpeza.core.map.data

import android.text.Spannable

data class PlaceSuggestion(
    val placeId: String,          // O ID único do lugar para buscar a localização depois
    val primaryText: Spannable,   // Ex: "Avenida Paulista"
    val secondaryText: Spannable,
    val distance: String? = null
)
