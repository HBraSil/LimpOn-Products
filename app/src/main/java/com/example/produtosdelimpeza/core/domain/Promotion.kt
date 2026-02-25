package com.example.produtosdelimpeza.core.domain

import com.example.produtosdelimpeza.core.domain.model.DiscountType

data class Promotion(
    val id: String = "",
    val storeId: String = "",
    val discountType: DiscountType = DiscountType.NONE, // talvez mudar isso para uma sealed class com data classes
    val discountValue: Int = 0,
    val expiration: Int = 0,
    val category: String = ""
)