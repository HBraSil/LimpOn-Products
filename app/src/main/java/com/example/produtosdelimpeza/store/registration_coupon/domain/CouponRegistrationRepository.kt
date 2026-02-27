package com.example.produtosdelimpeza.store.registration_coupon.domain

import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Coupon


interface CouponRegistrationRepository {
    suspend fun createCoupon(coupon: Coupon): FirebaseResult
    fun signOut()
}