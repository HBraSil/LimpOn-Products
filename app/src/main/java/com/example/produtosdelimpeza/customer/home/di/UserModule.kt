package com.example.produtosdelimpeza.customer.home.di

import com.example.produtosdelimpeza.customer.home.data.UserRepositoryImpl
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}