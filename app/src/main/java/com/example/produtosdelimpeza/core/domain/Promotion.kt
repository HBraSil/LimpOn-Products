package com.example.produtosdelimpeza.core.domain

import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer

data class Promotion(
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",
    val expirationOffer: ExpirationOffer = ExpirationOffer.NONE,
    val category: String = ""
)