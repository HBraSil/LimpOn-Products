package com.example.produtosdelimpeza.store.profile.edit_profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.store.dashboard.domain.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditStoreProfileViewModel @Inject constructor(
    private val repository: StoreRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditStoreProfileUiState())
    val uiState = _uiState.asStateFlow()


    init {
        observeStore()
    }

    private fun observeStore() {
        viewModelScope.launch {
            repository.currentStore.collect { store ->
                Log.d("EditStoreProfileViewModel", "Store: ${store?.name ?: "n veio nome"}")
                _uiState.update {
                    it.copy(
                        originalStore = store,
                        editableStore = store,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun updateStoreName(name: String) {
        _uiState.update {
            it.copy(editableStore = it.editableStore?.copy(name = name))
        }
    }

    fun saveName(name: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = repository.updateName(
                id = uiState.value.editableStore?.id ?: "id not found",
                name = name
            )
            Log.d("EditStoreProfileViewModel", "id: ${uiState.value.editableStore?.id}")

            if (result.isSuccess) {
                _uiState.update { it.copy(success = true, isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        error = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun reset() {
        _uiState.update { it.copy(success = false) }
    }
}

data class EditStoreProfileUiState(
    val isLoading: Boolean = false,
    val originalStore: Store? = Store(),
    val editableStore: Store? = Store(),
    val error: String? = null,
    val success: Boolean = false,
)