package com.example.produtosdelimpeza.customer.profile.domain

import com.example.produtosdelimpeza.core.domain.model.Store


interface ProfileScreenRepository {
    suspend fun getStores(): List<Store>
    fun signOut()
}
