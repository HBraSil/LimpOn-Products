package com.example.produtosdelimpeza.core.domain

import java.util.UUID

data class Coupon(
    val id: String = UUID.randomUUID().toString(),
    val couponCode: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountValue: String = "",
    val validityType: ValidityType = ValidityType.NONE
)

enum class DiscountType {
    PERCENTAGE, FIXED, NONE
}

enum class ValidityType {
    DAYS_7, DAYS_15, DAYS_30, CUSTOM, NONE
}
