package com.example.produtosdelimpeza.store.dashboard.coupon_registration.data

import android.accounts.NetworkErrorException
import android.net.http.NetworkException
import android.util.Log
import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Coupon
import com.example.produtosdelimpeza.store.dashboard.coupon_registration.domain.CouponRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(
    private val firebaseStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val networkChecker: NetworkChecker
): CouponRepository {
    override suspend fun createCoupon(coupon: Coupon): AppResult<Boolean> {
        return try {

            if (!networkChecker.isInternetAvailable()) {
                return AppResult.Error.Network
            }

            val userUid = firebaseAuth.currentUser?.uid ?: return AppResult.Error.SessionExpired

            val couponId = UUID.randomUUID().toString()

            firebaseStore.collection("users")
                .document(userUid)
                .collection("coupons")
                .document(couponId)
                .set(coupon.copy(id = couponId))
                .await()
            AppResult.Success(true)
        } catch (e: IOException) {
                AppResult.Error.Network
        } catch (e: Exception) {
                AppResult.Error.Unknown(e.message)
        }
    }
}