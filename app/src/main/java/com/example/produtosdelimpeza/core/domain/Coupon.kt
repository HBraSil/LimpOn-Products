package com.example.produtosdelimpeza.core.domain

import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.Expiration
import java.util.UUID

data class Coupon(
    val id: String = UUID.randomUUID().toString(),
    val couponCode: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",
    val expiration: Expiration = Expiration.NONE,
    val category: String = ""
)