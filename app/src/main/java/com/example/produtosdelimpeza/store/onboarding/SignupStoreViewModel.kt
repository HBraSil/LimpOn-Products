package com.example.produtosdelimpeza.store.onboarding

import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.EmailValidator
import com.example.produtosdelimpeza.core.validation.NameValidator
import com.example.produtosdelimpeza.store.onboarding.domain.SignUpStoreFormState
import com.example.produtosdelimpeza.store.onboarding.domain.SignupStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SignupStoreViewModel @Inject constructor(
    private val signupStoreRepository: SignupStoreRepository
): ViewModel() {
    private val _formState = MutableStateFlow(SignUpStoreFormState())
    val formState = _formState.asStateFlow()

    fun updateName(name: String) {
        val isNameValid = NameValidator.isNameValid(name)
        _formState.value = _formState.value.copy(
            storeName = FieldState(field = name, isValid = isNameValid, error = if (!isNameValid) "Nome é obrigatório" else null)
        )
    }

    fun updateEmail(email: String) {
        val isEmailValid = EmailValidator.isEmailValid(email)
        _formState.value = _formState.value.copy(
            email = FieldState(field = email, isValid = isEmailValid, error =  if (!isEmailValid) "Email inválido" else null)
        )
    }

    fun updatePhone(phone: String) {

        _formState.value = _formState.value.copy(
            phone = FieldState(field = phone)
        )
    }

}