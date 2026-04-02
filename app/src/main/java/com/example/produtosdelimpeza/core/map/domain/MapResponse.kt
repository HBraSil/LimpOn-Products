package com.example.produtosdelimpeza.core.map.domain

sealed class MapResponse {
    data class Success(
        val latitude: Double,
        val longitude: Double
    ) : MapResponse()

    data class Error(val errorMessage: String) : MapResponse()
}
