package com.example.produtosdelimpeza.store.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.EmailValidator
import com.example.produtosdelimpeza.core.validation.NameValidator
import com.example.produtosdelimpeza.core.validation.PhoneValidator
import com.example.produtosdelimpeza.store.onboarding.domain.SignUpStoreFormState
import com.example.produtosdelimpeza.store.onboarding.domain.SignupStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class SignUpStoreViewModel @Inject constructor(
    private val signupStoreRepository: SignupStoreRepository,
) : ViewModel() {
    var formState by mutableStateOf(SignUpStoreFormState())
    fun updateName(name: String) {
        val isNameValid = NameValidator.isNameValid(name)
        formState = formState.copy(
                storeName = FieldState(
                    field = name,
                    isValid = isNameValid,
                    error = if (!isNameValid) "Nome é obrigatório" else null
                )
            )
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
    }
}