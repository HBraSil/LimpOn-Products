package com.example.produtosdelimpeza.core.domain

import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer
import java.util.UUID

data class Coupon(
    val id: String = UUID.randomUUID().toString(),
    val couponCode: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",
    val expirationOffer: ExpirationOffer = ExpirationOffer.NONE,
    val category: String = ""
)