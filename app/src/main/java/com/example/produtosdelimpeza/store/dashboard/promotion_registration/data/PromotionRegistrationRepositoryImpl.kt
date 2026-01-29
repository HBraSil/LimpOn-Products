package com.example.produtosdelimpeza.store.dashboard.promotion_registration.data

import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class PromotionRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : PromotionRegistrationRepository {
    override suspend fun createPromotion(promotion: Promotion): Result<Boolean> {
        return try {
            val userUid = firebaseAuth.currentUser?.uid ?: throw Exception("Usuário não autenticado")
            val promotionId = UUID.randomUUID().toString()

            firestore.collection("users")
                .document(userUid)
                .collection("promotions")
                .document(promotionId)
                .set(promotion.copy(id = promotionId))
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}