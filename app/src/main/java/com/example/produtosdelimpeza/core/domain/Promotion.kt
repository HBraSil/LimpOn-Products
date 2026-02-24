package com.example.produtosdelimpeza.core.domain

import com.example.produtosdelimpeza.core.domain.model.DiscountType
import com.example.produtosdelimpeza.core.domain.model.Expiration

data class Promotion(
    val id: String = "",
    val storeId: String = "",
    val discountType: DiscountType = DiscountType.NONE, // talvez mudar isso para uma sealed class com data classes
    val discountValue: Int = 0,
    val expiresAt: Expiration = Expiration.NONE,
    val category: String = ""
)