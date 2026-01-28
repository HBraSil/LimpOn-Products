package com.example.produtosdelimpeza.store.dashboard.coupon_registration.data

import android.util.Log
import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.CouponRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(
    private val firebaseStore: FirebaseFirestore
): CouponRepository {

    override suspend fun createCoupon(coupon: Coupon) {
        try {
            firebaseStore.collection("coupons").add(coupon).await()
        } catch (e: Exception) {
            Log.e("CouponRepository", "Error inserting coupon", e)
        }
    }
}