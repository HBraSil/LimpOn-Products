package com.example.produtosdelimpeza.customer.search.di

import com.example.produtosdelimpeza.customer.search.data.SearchRepositoryImpl
import com.example.produtosdelimpeza.customer.search.domain.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {

    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

}