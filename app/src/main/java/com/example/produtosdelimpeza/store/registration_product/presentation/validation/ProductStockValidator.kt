package com.example.produtosdelimpeza.store.registration_product.presentation.validation

import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.core.presentation.UiText

object ProductStockValidator {
    fun isValid(stock: String): UiText.StringResource? {
        if (stock.toInt() <= 0) return UiText.StringResource(R.string.product_stock_invalid)


        return null
    }
}
