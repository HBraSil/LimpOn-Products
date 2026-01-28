package com.example.produtosdelimpeza.store.dashboard.promotion_registration.data

import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PromotionRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PromotionRegistrationRepository {
    override suspend fun createPromotion(promotion: Promotion): Result<Boolean> {
        return try {
            firestore.collection("promotions").add(promotion).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}