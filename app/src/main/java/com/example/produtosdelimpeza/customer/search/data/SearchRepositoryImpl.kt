package com.example.produtosdelimpeza.customer.search.data

import com.example.produtosdelimpeza.customer.search.domain.SearchRepository
import javax.inject.Inject


class SearchRepositoryImpl @Inject constructor(
    private val localStorage: SearchLocalDataStorage
): SearchRepository {
    override suspend fun saveSearchHistory(history: Set<String>) {
        localStorage.saveSearchHistory(history)
    }

    override suspend fun getSearchHistory(): Set<String> {
        return localStorage.getSearchHistory()
    }
}

