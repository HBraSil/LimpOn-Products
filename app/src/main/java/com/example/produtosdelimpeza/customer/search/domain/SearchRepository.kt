package com.example.produtosdelimpeza.customer.search.domain

import androidx.datastore.preferences.core.Preferences


interface SearchRepository{
    suspend fun saveSearchHistory(history: Set<String>)

    suspend fun getSearchHistory(): Set<String>


}