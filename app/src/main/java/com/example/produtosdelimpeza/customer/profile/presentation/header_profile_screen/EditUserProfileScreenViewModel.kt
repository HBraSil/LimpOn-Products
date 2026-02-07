package com.example.produtosdelimpeza.customer.profile.presentation.header_profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.data.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditUserProfileScreenViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : ViewModel() {
    val originalUser: StateFlow<User> = userRepository.getUser()
        .map { it ?: User() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            User()
        )


    private val _formState = MutableStateFlow(User())
    val formState: MutableStateFlow<User> = _formState

    private val _showSaveButton = MutableStateFlow(false)
    val hasChanges: MutableStateFlow<Boolean> = _showSaveButton


    init {
        viewModelScope.launch {
            originalUser.collect { user ->
                _formState.value = user
            }
        }
    }

    fun onNameChanged(newName: String) {
        _formState.update { currentState ->
            currentState.copy(name = newName)
        }


        val hasChanges = _formState.value != originalUser
        _showSaveButton.value = hasChanges
    }

    fun onEmailChanged(newEmail: String) {
        _formState.update { currentState ->
            currentState.copy(email = newEmail)
        }
    }

    fun onPhoneChanged(newPassword: String) {
        _formState.update { currentState ->
            currentState.copy(phone = newPassword)
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }
}