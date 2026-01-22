package com.example.produtosdelimpeza.customer.profile.presentation

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.customer.profile.data.ProfileScreenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileRepository: ProfileScreenRepository
) : ViewModel() {
    fun signOut() = profileRepository.signOut()
}