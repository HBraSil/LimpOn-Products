package com.example.produtosdelimpeza.customer.home.domain

import com.example.produtosdelimpeza.core.domain.model.Store

interface HomeRepository {
    suspend fun getStores(): List<Store>
}