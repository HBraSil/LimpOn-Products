package com.example.produtosdelimpeza.customer.onboarding.domain

import com.example.produtosdelimpeza.core.domain.model.Store

interface SignupStoreRepository {
    suspend fun createStore(store: Store): Result<Boolean>
}