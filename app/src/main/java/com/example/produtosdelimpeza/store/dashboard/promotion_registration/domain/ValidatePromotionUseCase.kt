package com.example.produtosdelimpeza.store.dashboard.promotion_registration.domain

import com.example.produtosdelimpeza.core.domain.Promotion
import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer
import javax.inject.Inject

class ValidatePromotionUseCase @Inject constructor() {
    operator fun invoke(promo: Promotion): Boolean {
        val formIsValid = with(promo) {
            discountValue.isNotBlank() &&
            category.isNotBlank() &&
            discountType != DiscountType.NONE &&
            expirationOffer != ExpirationOffer.NONE
        }

        return formIsValid
    }
}