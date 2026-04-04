package com.example.produtosdelimpeza.core.map.domain

import com.google.android.libraries.places.api.model.AutocompletePrediction


interface MapRepository {
    suspend fun fetchUserLocation(): MapResponse
    suspend fun checkLocationSettings(): LocationSettingsResult
    suspend fun searchPlaces(query: String): List<AutocompletePrediction>
}