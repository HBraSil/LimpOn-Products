package com.example.produtosdelimpeza.store.onboarding.di

import com.example.produtosdelimpeza.store.onboarding.data.SignupStoreRepositoryImpl
import com.example.produtosdelimpeza.store.onboarding.domain.SignupStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreModule {
    @Singleton
    @Binds
    abstract fun bindStoreRepository(impl: SignupStoreRepositoryImpl): SignupStoreRepository
}