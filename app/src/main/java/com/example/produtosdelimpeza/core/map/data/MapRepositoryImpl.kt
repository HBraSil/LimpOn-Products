package com.example.produtosdelimpeza.core.map.data

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.produtosdelimpeza.core.map.domain.LocationSettingsResult
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import com.example.produtosdelimpeza.core.map.domain.MapResponse
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
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

                 if (lastLocation != null) {
                    Log.d("ATENÇAO1", "CAIU AQUI: ${lastLocation.latitude} ${lastLocation.longitude}")
                     return MapResponse.Success(
                         lastLocation.latitude,
                         lastLocation.longitude
                     )
                 }

                 val currentLocation = fusedLocationProviderClient
                     .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                     .await()

                 Log.d("ATENÇAO1", "CAIU AQUI2: ${currentLocation.latitude} ${currentLocation.longitude}")
                 return MapResponse.Success(currentLocation.latitude, currentLocation.longitude)
             } else {
                 MapResponse.Error("true")
             }
         } catch (e: Exception) {
             MapResponse.Error("Location permission is not granted.")
         }
     }


    override suspend fun checkLocationSettings(): LocationSettingsResult {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

        val client = LocationServices.getSettingsClient(context)

        return try {
            client.checkLocationSettings(builder).await()
            LocationSettingsResult.Enabled
        } catch (exception: Exception) {
            if (exception is ResolvableApiException) {
                LocationSettingsResult.ResolutionNeeded(exception)
            } else {
                LocationSettingsResult.Error(exception)
            }
        }
    }
}
