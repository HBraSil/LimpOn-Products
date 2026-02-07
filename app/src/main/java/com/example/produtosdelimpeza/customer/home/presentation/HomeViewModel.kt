package com.example.produtosdelimpeza.customer.home.presentation

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: UserRepository
) : ViewModel() {

    val user: StateFlow<User> = repository.getUser()
        .map { it ?: User() }
        .stateIn(
            CoroutineScope(SupervisorJob() + Dispatchers.IO),
            SharingStarted.Eagerly,
            User()
        )

    /*init {
        viewModelScope.launch {
            userRepository.syncUser()
        }
    }*/
}