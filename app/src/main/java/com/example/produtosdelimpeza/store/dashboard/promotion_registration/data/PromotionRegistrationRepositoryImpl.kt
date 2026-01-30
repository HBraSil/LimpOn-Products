package com.example.produtosdelimpeza.store.dashboard.promotion_registration.data

import com.example.produtosdelimpeza.core.data.system.NetworkChecker
import com.example.produtosdelimpeza.core.domain.AppResult
import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class PromotionRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val networkChecker: NetworkChecker
) : PromotionRegistrationRepository {
    override suspend fun createPromotion(promotion: Promotion): AppResult<Boolean> {
        return try {
            if (!networkChecker.isInternetAvailable()) {
                return AppResult.Error.Network
            }

            val userUid = firebaseAuth.currentUser?.uid ?: return AppResult.Error.SessionExpired

            firestore.collection("users")
                .document(userUid)
                .collection("promotions")
                .document(promotion.id)
                .set(promotion)
                .await()

            AppResult.Success(true)
        } catch (e: IOException) {
            AppResult.Error.Network
        }  catch (e: Exception) {
            AppResult.Error.Unknown(e.message)
        }
    }
}