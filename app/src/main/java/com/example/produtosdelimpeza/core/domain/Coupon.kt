package com.example.produtosdelimpeza.core.domain

import java.util.UUID

data class Coupon(
    val id: String = UUID.randomUUID().toString(),
    val couponCode: String = "",
    val discountType: String = "",
    val discountValue: Int = 0,
    val expiration: Int = 0,
    val category: String = ""
)