package com.example.produtosdelimpeza.core.auth.presentation.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.auth.data.LoginResponse
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.auth.domain.validation.LoginValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var loginFormState by mutableStateOf(LoginFormState())

    private var _passwordHidden = MutableStateFlow(true)
    var passwordHidden: StateFlow<Boolean> = _passwordHidden.asStateFlow()


    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

 /*   fun reset() {
        _loginUiState.update { LoginUiState() }
    }*/

    fun updateEmail(email: String) {
        val isEmailValidate = LoginValidators.isEmailValid(email)
        loginFormState = loginFormState.copy(
            email = loginFormState.email.copy(
                field = email,
                error = if (isEmailValidate) null else "Email inválido",
                isValid = isEmailValidate
            )
        )
    }

    fun updatePassword(password: String) {
        val isEmailValidate = LoginValidators.isValidPassword(password)
        loginFormState = loginFormState.copy(
            password = loginFormState.email.copy(
                field = password,
                error = if (isEmailValidate) null else "Senha deve ter no mínimo 6 caracteres",
                isValid = isEmailValidate
            )
        )
    }

    fun updateRememberMe(rememberMe: Boolean) {
        loginFormState = loginFormState.copy(
            rememberMe = rememberMe
        )
    }

    fun changePasswordVisibility() {
        _passwordHidden.value = !_passwordHidden.value
    }


    fun loginWithEmailAndPassword() {
        _loginUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // 1. Chama o Repositório
            val result = authRepository.signInWithEmailAndPassword(loginFormState.email.field, loginFormState.password.field)

            when(result) {
                is LoginResponse.Success -> {
                    _loginUiState.update { it.copy(goToHome = true, isLoading = false) }
                }
                is LoginResponse.Error -> {
                    _loginUiState.value = LoginUiState(error = result.error)
                }
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            when(val result = authRepository.signInWithGoogle()) {
                is LoginResponse.Success -> {
                    _loginUiState.update { it.copy(goToHome = true, isLoading = false) }
                }
                is LoginResponse.Error -> {
                    _loginUiState.value = LoginUiState(error = result.error)
                }
            }
        }
    }

    fun cleanErrorMessage() {
        _loginUiState.update { it.copy(error = null) }
    }
}