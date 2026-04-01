package com.example.produtosdelimpeza.core.map.di

import com.example.produtosdelimpeza.core.map.data.MapRepositoryImpl
import com.example.produtosdelimpeza.core.map.domain.MapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapModule {
     @Binds
     @Singleton
     abstract fun bindMapRepository(mapRepositoryImpl: MapRepositoryImpl): MapRepository
}