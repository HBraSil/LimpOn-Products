package com.example.produtosdelimpeza.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DeepLinkViewModel @Inject constructor() : ViewModel() {

    private val _receivedLink = MutableStateFlow<String?>(null)
    val receivedLink: StateFlow<String?> = _receivedLink.asStateFlow()

    fun onDeepLinkReceived(link: String) {
        Log.d("DEEPL", "received1: ${_receivedLink.value}")
        _receivedLink.value = link
        Log.d("DEEPL", "received2: ${_receivedLink.value}")
    }

    fun consume() {
        _receivedLink.value = null
    }
}
