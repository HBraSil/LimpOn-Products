package com.example.produtosdelimpeza.core.map.domain

sealed class MapResponse {
    data class LatiLongi(
        val latitude: Double,
        val longitude: Double
    ) : MapResponse()

    data class MissingPermission(val error: Boolean) : MapResponse()

    data class Error(val errorMessage: String) : MapResponse()
}
