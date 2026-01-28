package com.example.produtosdelimpeza.store.dashboard.promotion_registration.di

import com.example.produtosdelimpeza.store.dashboard.promotion_registration.data.PromotionRegistrationRepositoryImpl
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
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