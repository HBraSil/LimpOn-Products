package com.example.produtosdelimpeza.dashboard.product_registration.domain

import com.example.produtosdelimpeza.core.data.Product

interface ProductRegistrationRepository {

    suspend fun registerProduct(product: Product)
}