package com.example.produtosdelimpeza.core.domain

data class Coupon(
    val id: String = "",
    val storeId: String = "",
    val couponCode: String = "",
    val discountType: String = "",
    val discountValue: Int = 0,
    val expiration: Int = 0,
    val category: String = ""
)