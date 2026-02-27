package com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain

import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Coupon


interface CouponRegistrationRepository {
    suspend fun createCoupon(coupon: Coupon): FirebaseResult
    fun signOut()
}