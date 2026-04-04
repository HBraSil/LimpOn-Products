package com.example.produtosdelimpeza.core.di

import android.content.Context
import android.util.Log
import com.example.produtosdelimpeza.BuildConfig
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GoogleModule {
    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        Log.d("GOOGLE_API_KEY", "ALOHAPlace")
        if (!Places.isInitialized()) {
            Log.d("GOOGLE_API_KEY", BuildConfig.X_SECRET_KEY)
            Places.initialize(context, BuildConfig.X_SECRET_KEY)
        }
        return Places.createClient(context)
    }
}