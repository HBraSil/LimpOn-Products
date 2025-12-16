package com.example.produtosdelimpeza.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.data.ProfileScreenRepository
import com.example.produtosdelimpeza.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileRepository: ProfileScreenRepository
) : ViewModel() {

    fun signOut() {
        profileRepository.signOut()
    }
}

