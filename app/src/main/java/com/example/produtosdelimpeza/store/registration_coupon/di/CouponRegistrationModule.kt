package com.example.produtosdelimpeza.store.registration_coupon.di

import com.example.produtosdelimpeza.store.registration_coupon.data.CouponRegistrationRepositoryImpl
import com.example.produtosdelimpeza.store.registration_coupon.domain.CouponRegistrationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CouponRegistrationModule {
    @Binds
    @Singleton
    abstract fun bindCouponRepository(
        couponRegistrationRepositoryImpl: CouponRegistrationRepositoryImpl
    ): CouponRegistrationRepository
}