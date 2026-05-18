package com.example.produtosdelimpeza.core.map.presentation

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.maps.model.LatLng

sealed interface MapUiEvent {
    data class RequestLocationResolution(val exception: ResolvableApiException) : MapUiEvent
    data class OnCenterLocationClick(val centerLocation: LatLng) : MapUiEvent
}