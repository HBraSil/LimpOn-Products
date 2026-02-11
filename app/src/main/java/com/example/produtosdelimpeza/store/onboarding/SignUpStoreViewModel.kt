package com.example.produtosdelimpeza.store.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.domain.model.Store
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.EmailValidator
import com.example.produtosdelimpeza.core.validation.NameValidator
import com.example.produtosdelimpeza.core.validation.PhoneValidator
import com.example.produtosdelimpeza.store.onboarding.domain.SignUpStoreFormState
import com.example.produtosdelimpeza.store.onboarding.domain.SignUpStoreUiState
import com.example.produtosdelimpeza.store.onboarding.domain.SignupStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpStoreViewModel @Inject constructor(
    private val signupStoreRepository: SignupStoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpStoreUiState())
    val uiState: StateFlow<SignUpStoreUiState> = _uiState.asStateFlow()

    var formState by mutableStateOf(SignUpStoreFormState())
    private val _isButtonValid = MutableStateFlow(false)
    val isButtonValid = _isButtonValid.asStateFlow()


    fun updateName(name: String) {
        val isNameValid = NameValidator.isNameValid(name)
        formState = formState.copy(
                storeName = FieldState(
                    field = name,
                    isValid = isNameValid,
                    error = if (!isNameValid) "Nome é obrigatório" else null
                )
            )

        updateConfirmationButton()
    }


    fun updateEmail(email: String) {
        val isEmailValid = EmailValidator.isEmailValid(email)
        formState = formState.copy(
                email = FieldState(
                    field = email,
                    isValid = isEmailValid,
                    error = if (!isEmailValid) "Email inválido" else null
                )
            )

        updateConfirmationButton()
    }


    fun updateDescription(description: String) {
        formState = formState.copy(
            description = FieldState(
                field = description,
                isValid = true,
                error = null
            )
        )
    }


    fun updateCategory(category: String) {
        formState = formState.copy(
            category = FieldState(
                field = category,
                isValid = true,
                error = null
            )
        )
    }


    fun updatePhone(phone: String) {
        val isPhoneValid = PhoneValidator.isValid(phone)
        formState = formState.copy(
                phone = FieldState(
                    field = phone,
                    isValid = isPhoneValid,
                    error = if (!isPhoneValid) "Telefone inválido" else null
                )
            )

        updateConfirmationButton()
    }


    fun updateConfirmationButton() {
        val isButtonValid = with(formState) {
            storeName.isValid
            && email.isValid
            && phone.isValid
        }

        _isButtonValid.value = isButtonValid
    }


    fun createStore() {
        _uiState.update { it.copy(isLoading = true) }

        formState = formState.copy()
        val store = Store(
            name = formState.storeName.field,
            email = formState.email.field,
            phone = formState.phone.field,
            description = formState.description.field,
            category = formState.category.field
        )
        viewModelScope.launch{
            val result = signupStoreRepository.createStore(store)

            if (result.isSuccess) _uiState.update { it.copy(goToHome = true) }
            else _uiState.update { it.copy(error = "Erro ao criar loja") }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}