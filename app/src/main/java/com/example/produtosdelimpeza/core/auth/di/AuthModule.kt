package com.example.produtosdelimpeza.core.auth.di

import com.example.produtosdelimpeza.core.auth.data.AuthRepositoryImpl
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun providesRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}