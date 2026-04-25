package com.example.produtosdelimpeza.core.map.data


data class PlaceSuggestionEntity(
    val placeId: String = "",
    val primaryText: String,
    val secondaryText: String,
    val distance: String? = null
)