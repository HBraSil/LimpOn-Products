package com.example.produtosdelimpeza.store.profile.presentation

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreProfileViewModel @Inject constructor(
    storeRepository: StoreRepository,
): ViewModel() {
    val storeProfile = storeRepository.currentStore
}