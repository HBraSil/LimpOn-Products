package com.example.produtosdelimpeza.customer.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import com.example.produtosdelimpeza.customer.profile.data.ProfileScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileRepository: ProfileScreenRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user: StateFlow<User> = userRepository.getUser()
        .map { it ?: User() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
        )

    fun signOut() = profileRepository.signOut()
}
