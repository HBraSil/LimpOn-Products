package com.example.produtosdelimpeza.store.dashboard.promotion_registration.data

import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain.PromotionRegistrationRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class PromotionRegistrationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PromotionRegistrationRepository {
    override suspend fun createPromotion(promotion: Promotion): Result<Boolean> {

        return Result.success(true)
    }
}