package com.example.produtosdelimpeza.core.di

import com.example.produtosdelimpeza.core.data.AuthSessionProviderImpl
import com.example.produtosdelimpeza.core.domain.AuthSessionProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthSessionModule {
    @Binds
    abstract fun bindAuthSessionProvider(impl: AuthSessionProviderImpl): AuthSessionProvider
}
