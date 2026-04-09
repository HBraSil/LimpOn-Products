package com.example.produtosdelimpeza.core.map.presentation

import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion
import com.google.android.gms.maps.model.LatLng


data class MapUiState(
    val isLoading: Boolean = false,
    val userLocation: LatLng? = null,
    val place: PlaceSuggestion? = null,
    val error: String? = null
)
