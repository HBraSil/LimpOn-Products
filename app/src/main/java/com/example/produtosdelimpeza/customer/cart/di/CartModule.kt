package com.example.produtosdelimpeza.customer.cart.di

import com.example.produtosdelimpeza.customer.cart.data.CartRepositoryImpl
import com.example.produtosdelimpeza.customer.cart.domain.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CartModule {
    @Binds
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository
}