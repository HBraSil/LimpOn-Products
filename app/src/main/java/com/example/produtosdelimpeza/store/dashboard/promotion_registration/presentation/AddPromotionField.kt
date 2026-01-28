package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation

import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer


sealed class AddPromotionField {
    data class DiscountTypeField(val value: DiscountType) : AddPromotionField()
    data class DiscountValueField(val value: String) : AddPromotionField()
    data class DurationField(val value: ExpirationOffer) : AddPromotionField()
    data class CategoryField(val value: String) : AddPromotionField()
}