package com.example.produtosdelimpeza.customer.catalog.di

import com.example.produtosdelimpeza.customer.catalog.data.CatalogRepositoryImpl
import com.example.produtosdelimpeza.customer.catalog.domain.CatalogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class CatalogModule {
    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        catalogRepositoryImpl: CatalogRepositoryImpl
    ): CatalogRepository
}