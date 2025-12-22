package com.example.produtosdelimpeza.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.data.NavigationLastUserModeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class NavigationLastUserModeViewModel @Inject constructor(
    private val navigationLastUserModeRepository: NavigationLastUserModeRepository
) : ViewModel() {

    private val _lastUserMode = MutableStateFlow<ProfileMode?>(null)
    val lastUserMode: StateFlow<ProfileMode?> = _lastUserMode.asStateFlow()

    init {
        viewModelScope.launch {
            navigationLastUserModeRepository.lastActiveProfile.collect { modeUser ->
                _lastUserMode.value = modeUser
            }
        }
    }

    fun saveLastUserMode(profileMode: ProfileMode) {
        viewModelScope.launch {
            navigationLastUserModeRepository.saveLastUserMode(profileMode)
        }

    }
}