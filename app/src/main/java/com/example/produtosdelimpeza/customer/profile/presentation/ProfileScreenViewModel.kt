package com.example.produtosdelimpeza.customer.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.data.LastUserModeLocalStorage
import com.example.produtosdelimpeza.core.domain.model.ProfileMode
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import com.example.produtosdelimpeza.customer.profile.data.ProfileScreenRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileRepository: ProfileScreenRepositoryImpl,
    private val lastUserModeLocalStorage: LastUserModeLocalStorage,
    userRepository: UserRepository
) : ViewModel() {
    val user: StateFlow<User> = userRepository.getUser()
        .map { it ?: User() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
        )

    private val _allStores = MutableStateFlow<List<Store>>(emptyList())
    val allStores: StateFlow<List<Store>> = _allStores.asStateFlow()

    init {
        viewModelScope.launch {
            _allStores.value = profileRepository.getStores()
            Log.d("ProfileScreenViewModel", "FOI ACIONADO -- Stores: ${_allStores.value}")
        }
    }

    fun saveLastUserMode(storeId: String) {
        viewModelScope.launch {
            Log.d("ProfileScreenViewModel", "FOI ACIONADO -- StoreId: $storeId")
            lastUserModeLocalStorage.saveLastUserMode(ProfileMode.LoggedIn.StoreSection(storeId))
        }
    }

    fun signOut() = profileRepository.signOut()
}