package com.example.produtosdelimpeza.core.map.data


data class PlaceSuggestionEntity(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String,
    val etaMinutes: String = "",
    val distance: String? = null
)