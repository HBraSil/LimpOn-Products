package com.example.produtosdelimpeza.core.map.domain

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place


interface MapRepository {
    suspend fun fetchUserLocation(): MapResponse
    suspend fun checkLocationSettings(): LocationSettingsResult
    suspend fun searchPlaces(query: String, userLatLng: LatLng?): List<AutocompletePrediction>
    suspend fun getPlaceLatLng(placeId: String): Place
}