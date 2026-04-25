package com.example.produtosdelimpeza.core.map.presentation

import com.google.android.gms.maps.model.LatLng


data class MapUiState(
    val isLoading: Boolean = false,
    val userLocation: LatLng? = null,
    val place: PlaceSuggestion? = null,
    //val address: Address? = null,
    val placeSavedSuccessfully: Boolean = false,
    val error: String? = null
)
