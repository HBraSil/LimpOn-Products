package com.example.produtosdelimpeza.store.dashboard.product_registration.di

import com.example.produtosdelimpeza.store.dashboard.product_registration.data.ProductRegistrationRepositoryImpl
import com.example.produtosdelimpeza.store.dashboard.product_registration.domain.ProductRegistrationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductRegistrationModule {
    @Binds
    @Singleton
    abstract fun bindProductRegistrationRepository(
        productRegistrationRepositoryImpl: ProductRegistrationRepositoryImpl
    ): ProductRegistrationRepository

}