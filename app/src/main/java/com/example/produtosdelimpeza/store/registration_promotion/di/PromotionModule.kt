package com.example.produtosdelimpeza.store.registration_promotion.di

import com.example.produtosdelimpeza.store.registration_promotion.data.PromotionRegistrationRepositoryImpl
import com.example.produtosdelimpeza.store.registration_promotion.domain.PromotionRegistrationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PromotionModule {
    @Binds
    @Singleton
    abstract fun bindPromotionRegistrationRepository(
        promotionRegistrationRepositoryImpl: PromotionRegistrationRepositoryImpl
    ): PromotionRegistrationRepository
}