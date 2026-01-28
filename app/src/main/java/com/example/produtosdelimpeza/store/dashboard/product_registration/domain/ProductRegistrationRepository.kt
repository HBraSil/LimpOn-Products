package com.example.produtosdelimpeza.store.dashboard.product_registration.domain

import com.example.produtosdelimpeza.core.domain.Product


interface ProductRegistrationRepository {

    suspend fun registerProduct(product: Product): Result<Unit>
}