package com.example.produtosdelimpeza.store.registration_product.domain

import com.example.produtosdelimpeza.core.domain.FirebaseResult
import com.example.produtosdelimpeza.core.domain.Product


interface ProductRegistrationRepository {
    suspend fun registerProduct(product: Product): FirebaseResult
    fun signOut()
}