package com.example.produtosdelimpeza.store.dashboard.coupon_registration.data

import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.CouponRepository
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.presentation.AddCouponField
import com.example.produtosdelimpeza.store.dashboard.product_registration.presentation.AddProductField
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(
    private val firebaseStore: FirebaseFirestore
): CouponRepository {

    override suspend fun insertCoupon(coupon: Coupon) {
        firebaseStore.collection("coupons").add(coupon)
    }
}