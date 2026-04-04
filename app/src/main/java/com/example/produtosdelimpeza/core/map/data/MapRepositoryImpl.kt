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
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


@Singleton
class MapRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient,
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
                     return MapResponse.Success(
                         lastLocation.latitude,
                         lastLocation.longitude
                     )
                 }

                 val currentLocation = fusedLocationProviderClient
                     .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                     .await()

                 return MapResponse.Success(currentLocation.latitude, currentLocation.longitude)
             } else {
                 MapResponse.MissingPermission
             }
         } catch (e: Exception) {
             MapResponse.Error("Something went wrong: ${e.message}")
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

    override suspend fun searchPlaces(query: String): List<AutocompletePrediction> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        return try {
            val response = placesClient.findAutocompletePredictions(request).await()
            Log.d("MapRepositoryImpl", "Response: ${response.autocompletePredictions.size}")
            response.autocompletePredictions
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.d("MapRepositoryImpl", "Error: ${e.message}")
            emptyList()
        }
    }

}//15:51:20.410
