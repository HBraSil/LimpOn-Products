package com.example.produtosdelimpeza.store.registration_coupon.data

import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.store.registration_coupon.domain.CouponRegistrationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class CouponRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val networkChecker: NetworkChecker,
    private val userSession: LastUserModeLocalStorage
): CouponRegistrationRepository {
    override suspend fun createCoupon(coupon: Coupon): FirebaseResult {
        return try {
            if (!networkChecker.isInternetAvailable()) {
                return FirebaseResult.Error.Network("No internet connection")
            }

            val storeId = userSession.storeId.first() ?: return FirebaseResult.Error.Unknown("Store ID not found")

            val docRef = firestore.collection("coupons").document()
            val id = docRef.id

            val couponWithStoreId = coupon.copy(
                id = id,
                storeId = storeId
            )

            docRef.set(couponWithStoreId).await()

            FirebaseResult.Success(true)
        } catch (e: IOException) {
            FirebaseResult.Error.Network(e.message ?: "No internet connection")
        } catch (e: Exception) {
            FirebaseResult.Error.Unknown(e.message ?: "Unknown error")
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}