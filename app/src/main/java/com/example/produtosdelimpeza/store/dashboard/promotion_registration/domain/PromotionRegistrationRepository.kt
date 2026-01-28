package com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain

import com.example.produtosdelimpeza.core.domain.Promotion

interface PromotionRegistrationRepository {
    suspend fun createPromotion(promotion: Promotion): Result<Boolean>
}