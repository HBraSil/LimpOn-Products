package com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain

import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Coupon


interface CouponRepository {
    suspend fun createCoupon(coupon: Coupon): AppResult<Boolean>
    fun signOut()
}