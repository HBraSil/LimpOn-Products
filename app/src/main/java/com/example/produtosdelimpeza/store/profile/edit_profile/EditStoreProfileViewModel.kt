package com.example.produtosdelimpeza.store.profile.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditStoreProfileViewModel @Inject constructor(
    private val repository: StoreRepository
): ViewModel() {

    private val _updatedSuccessfully = MutableStateFlow<Boolean?>(null)
    val updatedSuccessfully = _updatedSuccessfully.asStateFlow()

    val store = repository.currentStore

    fun updateStoreName(id: String, name: String) {
        viewModelScope.launch {
            val result = repository.updateName(id, name)
            _updatedSuccessfully.value = result
        }
    }
}