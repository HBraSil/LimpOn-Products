package com.example.produtosdelimpeza.core.map.domain

import com.example.produtosdelimpeza.core.map.presentation.PlaceSuggestion
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place


interface MapRepository {
    suspend fun fetchUserLocation(): MapResponse
    suspend fun checkLocationSettings(): LocationSettingsResult
    suspend fun searchPlaces(query: String, latitude: Double?, longitude: Double?): List<AutocompletePrediction>
    suspend fun getPlaceWithId(placeId: String): Place
    suspend fun getPrimaryAndSecondaryFromLatLng(latitude: Double, longitude: Double): PlaceSuggestion?
    suspend fun savePlace(place: PlaceSuggestion): Result<Boolean>
}