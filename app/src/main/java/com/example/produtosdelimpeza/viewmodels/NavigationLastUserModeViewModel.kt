package com.example.produtosdelimpeza.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.commons.LastUserMode
import com.example.produtosdelimpeza.commons.ProfileMode
import com.example.produtosdelimpeza.data.NavigationLastUserModeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationLastUserModeViewModel @Inject constructor(
    private val navigationLastUserModeRepository: NavigationLastUserModeRepository
) : ViewModel() {

    private val _lastUserMode = MutableStateFlow(LastUserMode())
    val lastUserMode: StateFlow<LastUserMode> = _lastUserMode.asStateFlow()


    init {
        viewModelScope.launch {
            navigationLastUserModeRepository.lastActiveProfile.collect { modeUser ->
                if (modeUser == ProfileMode.CUSTOMER.mode) _lastUserMode.update { it.copy(currentMode = ProfileMode.CUSTOMER.mode) }
                else _lastUserMode.update { it.copy(currentMode = ProfileMode.STORE.mode) }
            }
        }
    }


    fun saveLastUserMode(profileMode: String) {
        viewModelScope.launch {
            Log.d("NavigationLastUserModeViewModel", "Saving last user mode: $profileMode")
            navigationLastUserModeRepository.saveLastUserMode(profileMode)
        }

    }
}