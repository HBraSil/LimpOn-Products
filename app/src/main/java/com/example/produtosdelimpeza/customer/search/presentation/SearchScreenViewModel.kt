package com.example.produtosdelimpeza.customer.search.presentation

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.customer.search.data.SearchRepositoryImpl
import com.example.produtosdelimpeza.customer.search.data.searchScreenDataStore
import com.example.produtosdelimpeza.customer.search.domain.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    private val searchHistoryManager: SearchRepository
) : ViewModel() {
    private val _history = mutableStateListOf<String>()

    val history: List<String> get() = _history

    init {
        viewModelScope.launch {
            _history.addAll(searchHistoryManager.getSearchHistory())
        }
    }


    fun addSearchItem(item: String) {
        if (item.isNotBlank() && !_history.contains(item)) {
            _history.add(0, item) // Adiciona no início da lista
            viewModelScope.launch {
                searchHistoryManager.saveSearchHistory(_history.toSet())
            }
        }
    }

    fun removeSearchItem(item: String) {
        _history.remove(item)
        viewModelScope.launch {
            searchHistoryManager.saveSearchHistory(_history.toSet())
        }
    }

    fun clearSearchHistory() {
        _history.clear()
        viewModelScope.launch {
            searchHistoryManager.saveSearchHistory(emptySet())
        }
    }
}