package com.example.produtosdelimpeza.core.map.data

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.tasks.await


@Singleton
class MapRepositoryImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @param:ApplicationContext private val context: Context
) : MapRepository {
     override suspend fun fetchUserLocation(): MapResponse {
         return try {
             if (
                 ContextCompat.checkSelfPermission(
                     context,
                     ACCESS_FINE_LOCATION
                 ) == PackageManager.PERMISSION_GRANTED
                 && ContextCompat.checkSelfPermission(
                     context,
                     ACCESS_COARSE_LOCATION
                 ) == PackageManager.PERMISSION_GRANTED
             ) {
                 val lastLocation = fusedLocationProviderClient.lastLocation.await()

                 if (lastLocation == null) {
                     Log.d("RETORno", "Location NULL")
                     return MapResponse.Error("Localização indisponível")
                 }

                 Log.d("RETORno", "${lastLocation.latitude}, ${lastLocation.longitude}")

                 return MapResponse.LatiLongi(
                     lastLocation.latitude,
                     lastLocation.longitude
                 )
             } else {
                 Log.d("RETORno", "NADA2")
                 MapResponse.MissingPermission(true)
             }
         } catch (e: Exception) {
             Log.d("RETORno", "NADA${e.localizedMessage}")
             MapResponse.Error("Location permission is not granted.")
         }
     }
}
