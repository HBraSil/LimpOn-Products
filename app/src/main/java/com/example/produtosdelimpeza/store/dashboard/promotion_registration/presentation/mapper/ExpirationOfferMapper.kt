package com.example.produtosdelimpeza.store.dashboard.promotion_registration.presentation.mapper

import androidx.annotation.StringRes
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.domain.model.ExpirationOffer

@StringRes
fun ExpirationOffer.toDisplayName(): Int = when (this) {
    ExpirationOffer.DAYS_7 -> R.string.seven_days
    ExpirationOffer.DAYS_15 -> R.string.fifteen_days
    ExpirationOffer.DAYS_30 -> R.string.thirty_days
    else -> R.string.custom
}
