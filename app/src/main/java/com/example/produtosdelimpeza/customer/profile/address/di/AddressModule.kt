package com.example.produtosdelimpeza.customer.profile.address.di

import com.example.produtosdelimpeza.customer.profile.address.data.AddressRepositoryImpl
import com.example.produtosdelimpeza.customer.profile.address.domain.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AddressModule {
    @Binds
    @Singleton
    abstract fun bindAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository
}