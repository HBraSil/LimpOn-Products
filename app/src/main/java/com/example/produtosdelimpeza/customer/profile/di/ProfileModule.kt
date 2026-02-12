package com.example.produtosdelimpeza.customer.profile.di

import com.example.produtosdelimpeza.customer.profile.data.ProfileScreenRepositoryImpl
import com.example.produtosdelimpeza.customer.profile.domain.ProfileScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {
    @Singleton
    @Binds
    abstract fun bindProfileScreenRepository(
        profileScreenRepositoryImpl: ProfileScreenRepositoryImpl
    ): ProfileScreenRepository
}