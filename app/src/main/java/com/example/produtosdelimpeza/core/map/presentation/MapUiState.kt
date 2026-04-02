package com.example.produtosdelimpeza.core.map.presentation

import com.google.android.gms.maps.model.LatLng


data class MapUiState(
    val isLoading: Boolean = false,
    val userLocation: LatLng? = null,
    val error: String? = null
)
