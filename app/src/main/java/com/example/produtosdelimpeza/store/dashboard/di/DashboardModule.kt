package com.example.produtosdelimpeza.store.dashboard.di

import com.example.produtosdelimpeza.store.dashboard.data.StoreRepositoryImpl
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {
    @Singleton
    @Binds
    abstract fun bindDashboardRepository(impl: StoreRepositoryImpl): StoreRepository
}