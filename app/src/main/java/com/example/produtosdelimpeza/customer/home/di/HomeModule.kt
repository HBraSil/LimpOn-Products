package com.example.produtosdelimpeza.customer.home.di

import com.example.produtosdelimpeza.customer.home.data.HomeRepositoryImpl
import com.example.produtosdelimpeza.customer.home.domain.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {
    @Binds
    @Singleton
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository
}