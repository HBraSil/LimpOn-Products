package com.example.produtosdelimpeza.di

import androidx.compose.ui.platform.LocalContext
import com.example.produtosdelimpeza.connection.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Define o escopo do módulo para todo o aplicativo
object FirebaseModule {

    @Provides
    @Singleton // Garante que haverá apenas uma instância de FirebaseAuth em todo o app
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton // Garante que haverá apenas uma instância de FirebaseAuth em todo o app
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}