package com.example.produtosdelimpeza.customer.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.UserSessionManager
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserSessionManager,
) : ViewModel() {

    val user: StateFlow<User> = userRepository.user

    /*init {
        viewModelScope.launch {
            userRepository.syncUser()
        }
    }*/
}