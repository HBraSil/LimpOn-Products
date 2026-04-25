package com.example.produtosdelimpeza.customer.home.domain

import com.example.produtosdelimpeza.core.domain.model.Address
import com.example.produtosdelimpeza.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getStores(): List<Store>
    fun getMainAddress(): Flow<Address?>
}