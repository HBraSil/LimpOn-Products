package com.example.produtosdelimpeza.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppModeViewModel @Inject constructor() : ViewModel() {

    var isSellerMode by mutableStateOf(false)
        private set

    fun switchToSellerMode() {
        isSellerMode = true
    }

    fun switchToClientMode() {
        isSellerMode = false
    }
}
