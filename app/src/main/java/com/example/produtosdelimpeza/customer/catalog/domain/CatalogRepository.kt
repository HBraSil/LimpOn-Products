package com.example.produtosdelimpeza.customer.catalog.domain

import com.example.produtosdelimpeza.core.domain.Product
import com.example.produtosdelimpeza.core.domain.model.Store

interface CatalogRepository {
    suspend fun fetchStore(storeId: String): Result<Store>
    suspend fun fetchProduct(sellerId: String): Result<List<Product>>
}