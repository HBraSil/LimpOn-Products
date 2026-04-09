package com.example.produtosdelimpeza.core.map.domain

import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.flow.Flow


interface MapRepository {
    suspend fun fetchUserLocation(): MapResponse
    suspend fun checkLocationSettings(): LocationSettingsResult
    suspend fun searchPlaces(query: String, userLatLng: LatLng?): List<AutocompletePrediction>
    suspend fun getPlaceLatLng(placeId: String): Place
    suspend fun savePlace(place: PlaceSuggestion)
    fun getSavedPlaces(): Flow<List<PlaceSuggestion>>
}