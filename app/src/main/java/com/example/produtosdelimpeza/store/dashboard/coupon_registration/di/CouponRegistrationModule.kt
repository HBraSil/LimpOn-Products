package com.example.produtosdelimpeza.store.dashboard.coupon_registration.di

import com.example.produtosdelimpeza.store.dashboard.coupon_registration.data.CouponRegistrationRepositoryImpl
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.CouponRegistrationRepository
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