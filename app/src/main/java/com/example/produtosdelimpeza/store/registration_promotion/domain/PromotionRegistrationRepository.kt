package com.example.produtosdelimpeza.store.registration_promotion.domain

import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Promotion

interface PromotionRegistrationRepository {
    suspend fun createPromotion(promotion: Promotion): FirebaseResult
}