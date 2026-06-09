package com.example.produtosdelimpeza.core.auth.di

import com.example.produtosdelimpeza.core.auth.data.AuthRepositoryImpl
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {
    @Binds
    @ViewModelScoped
    abstract fun providesAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}