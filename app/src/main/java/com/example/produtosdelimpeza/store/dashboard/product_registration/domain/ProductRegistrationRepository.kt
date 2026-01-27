package com.example.produtosdelimpeza.store.dashboard.product_registration.domain


interface ProductRegistrationRepository {

    suspend fun registerProduct(product: Product): Result<Unit>
}