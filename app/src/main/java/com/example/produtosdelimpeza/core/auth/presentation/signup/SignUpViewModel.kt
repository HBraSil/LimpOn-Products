package com.example.produtosdelimpeza.core.auth.presentation.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.auth.domain.validation.SignUpValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()


    var formState by mutableStateOf(UserFormState())


    fun updateName(name: String){
        val isNameValid = SignUpValidators.isNameValid(name)
        formState = formState.copy(
            name = FieldState(
                field = name,
                error = if (!isNameValid) "Nome é obrigatório" else null,
                isValid = isNameValid
            )
        )

        updateButtonSignUpState()
    }

    fun updateLastName(lastName: String){
        val isLastNameValid = SignUpValidators.isLastNameValid(lastName)
        formState = formState.copy(
            lastName = FieldState(
                field = lastName,
                error = if (!isLastNameValid) "Sobrenome é obrigatório" else null,
                isValid = isLastNameValid
            )
        )

        updateButtonSignUpState()
    }

    fun updateEmail(email: String){
        val isEmailValid = SignUpValidators.isEmailValid(email)
        formState = formState.copy(
            email = FieldState(
                field = email,
                error = if (!isEmailValid) "Email inválido" else null,
                isValid = isEmailValid
            )
        )
        updateButtonSignUpState()
    }

    fun updatePassword(password: String){
        val isPasswordValid = SignUpValidators.isValidPassword(password)
        formState = formState.copy(password = FieldState(
            field = password,
            error = if (!isPasswordValid) "Senha muito pequena" else null,
            isValid = isPasswordValid
        )
        )

        updateButtonSignUpState()
    }

    fun updatePasswordConfirm(password: String, confirmPassword: String){
        val isConfirmPasswordValid = SignUpValidators.isConfirmPasswordValid(password, confirmPassword)
        formState = formState.copy(confirmPassword = FieldState(
            field = confirmPassword,
            error = if (!isConfirmPasswordValid) "Senha precisa ser igual a anterior" else null,
            isValid = isConfirmPasswordValid
        )
        )

        updateButtonSignUpState()
    }

    private fun updateButtonSignUpState() {
        val formIsValid = with(formState) {
            email.isValid &&
            name.isValid &&
            lastName.isValid &&
            password.isValid &&
            confirmPassword.isValid
        }

        formState = formState.copy(
            formIsValid = formIsValid
        )
    }


    fun registerUser() {
        viewModelScope.launch {
            try {
                repository.registerUser(formState.name.field, formState.lastName.field, formState.email.field, formState.password.field)
            } catch (e: Exception) {
                Log.d("ERRO", "ERRO em registerUser na ViewModel: ${e.message}")
            }
        }
    }
}