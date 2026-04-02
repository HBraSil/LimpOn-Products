package com.example.produtosdelimpeza.core.map.domain

import com.google.android.gms.common.api.ResolvableApiException

sealed class LocationSettingsResult {
    object Enabled : LocationSettingsResult()
    data class ResolutionNeeded(val exception: ResolvableApiException) : LocationSettingsResult()
    data class Error(val exception: Exception) : LocationSettingsResult()
}
