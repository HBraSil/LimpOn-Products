package com.example.produtosdelimpeza.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationLastUserModeViewModel @Inject constructor(
    private val lastUserModeLocalStorage: LastUserModeLocalStorage
) : ViewModel() {

    private val _lastUserMode = MutableStateFlow<ProfileMode?>(null)
    val lastUserMode: StateFlow<ProfileMode?> = _lastUserMode.asStateFlow()


    init {
        viewModelScope.launch {
            lastUserModeLocalStorage.lastActiveProfile.collect { modeUser ->
                _lastUserMode.value = modeUser
            }
        }
    }

    fun saveLastUserMode(profileMode: ProfileMode) {
        viewModelScope.launch {
            lastUserModeLocalStorage.saveLastUserMode(profileMode)
        }
    }
}