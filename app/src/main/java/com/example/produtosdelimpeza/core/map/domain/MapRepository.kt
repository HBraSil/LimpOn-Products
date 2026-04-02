package com.example.produtosdelimpeza.core.map.domain


interface MapRepository {
    suspend fun fetchUserLocation(): MapResponse
    suspend fun checkLocationSettings(): LocationSettingsResult
}