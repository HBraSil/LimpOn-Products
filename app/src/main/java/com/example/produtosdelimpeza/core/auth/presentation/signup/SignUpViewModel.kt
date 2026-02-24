package com.example.produtosdelimpeza.core.auth.presentation.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.auth.data.LoginResponse
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.auth.presentation.AuthUiState
import com.example.produtosdelimpeza.core.presentation.FieldState
import com.example.produtosdelimpeza.core.validation.EmailValidator
import com.example.produtosdelimpeza.core.validation.NameValidator
import com.example.produtosdelimpeza.core.validation.PasswordValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()


    var formState by mutableStateOf(UserFormState())


    fun updateName(name: String){
        val isNameValid = NameValidator.isNameValid(name)
        formState = formState.copy(
            name = FieldState(
                field = name,
                error = isNameValid,
                isValid = isNameValid == null
            )
        )

        updateButtonSignUpState()
    }

    fun updateLastName(lastName: String){
        val isLastNameValid = NameValidator.isLastNameValid(lastName)
        formState = formState.copy(
            lastName = FieldState(
                field = lastName,
                error = isLastNameValid,
                isValid = isLastNameValid == null
            )
        )

        updateButtonSignUpState()
    }

    fun updateEmail(email: String){
        val isEmailValid = EmailValidator.validate(email)
        formState = formState.copy(
            email = FieldState(
                field = email,
                error = isEmailValid,
                isValid = isEmailValid == null
            )
        )
        updateButtonSignUpState()
    }

    fun updatePassword(password: String){
        val isPasswordValid = PasswordValidator.isValidPassword(password)
        formState = formState.copy(
            password = FieldState(
                field = password,
                error = isPasswordValid,
                isValid = isPasswordValid == null
            )
        )

        updateButtonSignUpState()
    }

    fun updatePasswordConfirm(password: String, confirmPassword: String){
        val isConfirmPasswordValid = PasswordValidator.isValidConfirmPassword(password, confirmPassword)
        formState = formState.copy(confirmPassword = FieldState(
            field = confirmPassword,
            error = isConfirmPasswordValid,
            isValid = isConfirmPasswordValid == null
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
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val result = repository.registerUser(formState.name.field, formState.lastName.field, formState.email.field, formState.password.field)

                when (result) {
                    is LoginResponse.Success -> _uiState.update { it.copy(success = true, isLoading = false) }
                    is LoginResponse.Error -> _uiState.update { it.copy(error = result.error) }
                    else -> {}
                }
            } catch (e: Exception) {
                Log.d("ERRO", "ERRO em registerUser na ViewModel: ${e.message}")
            }
        }
    }
}