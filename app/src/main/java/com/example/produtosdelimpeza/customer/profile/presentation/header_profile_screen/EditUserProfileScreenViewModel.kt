package com.example.produtosdelimpeza.customer.profile.presentation.header_profile_screen

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.domain.UserSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class EditUserProfileScreenViewModel @Inject constructor(
    userSessionManager: UserSessionManager
) : ViewModel() {
    val user = userSessionManager.user

}