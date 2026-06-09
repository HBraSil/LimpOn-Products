package com.example.produtosdelimpeza.core.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.auth.data.LoginResponse
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.auth.presentation.AuthUiState
import com.example.produtosdelimpeza.core.validation.EmailValidator
import com.example.produtosdelimpeza.core.validation.PasswordValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState = _loginFormState.asStateFlow()

    private var _passwordHidden = MutableStateFlow(true)
    var passwordHidden: StateFlow<Boolean> = _passwordHidden.asStateFlow()

    private val _authUiState = MutableStateFlow(AuthUiState())
    val loginUiState = _authUiState.asStateFlow()


    fun onEvent(event: LoginFormEvent) {
        when(event) {
            is LoginFormEvent.UpdateEmail -> updateEmail(event.email)
            is LoginFormEvent.UpdatePassword -> updatePassword(event.password)
            is LoginFormEvent.ChangePasswordVisibility -> changePasswordVisibility()
            is LoginFormEvent.LoginWithEmailAndPassword -> loginWithEmailAndPassword()
            is LoginFormEvent.LoginWithGoogle -> loginWithGoogle()
            is LoginFormEvent.CleanErrorMessage -> cleanErrorMessage()
        }
    }


    fun updateEmail(email: String) {
        val isEmailValidate = EmailValidator.validate(email)
        _loginFormState.update {
            it.copy(
                email = it.email.copy(
                    field = email,
                    error = isEmailValidate,
                    isValid = isEmailValidate == null
                )
            )
        }
    }
    fun updatePassword(password: String) {
        val isPasswordValidate = PasswordValidator.isValidPassword(password)
        _loginFormState.update {
            it.copy(
                password = it.password.copy(
                    field = password,
                    error = isPasswordValidate,
                    isValid = isPasswordValidate == null
                )
            )
        }
    }

    fun changePasswordVisibility() {
        _passwordHidden.value = !_passwordHidden.value
    }


    fun loginWithEmailAndPassword() {
        _authUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = authRepository.signInWithEmailAndPassword(
                email = _loginFormState.value.email.field,
                password = _loginFormState.value.password.field
            )

            when(result) {
                is LoginResponse.Success -> _authUiState.update { it.copy(success = true, isLoading = false) }
                is LoginResponse.Error -> _authUiState.value = AuthUiState(error = result.error)
                else -> {}
            }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            authRepository.signInWithGoogle().collect { result ->
                when(result) {
                    is LoginResponse.Loading -> _authUiState.update { it.copy(isLoading = true) }
                    is LoginResponse.Success -> _authUiState.update {
                        Log.d("LoginViewModel", "loginWithGoogle? ${it.success} -- erro? ${it.error}")
                        it.copy(success = true, isLoading = false) }
                    is LoginResponse.Error -> _authUiState.update {
                        Log.e("LoginViewModel", "loginWithGoogle: ${result.error}")
                        it.copy(error = result.error) }
                }
            }
        }
    }

    fun loginWithFacebook(token: String) {
        _authUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            authRepository.facebookLogin(token).collectLatest { response ->
                when (response) {
                    LoginResponse.Success -> {
                        _authUiState.update { it.copy(success = true, isLoading = false) }
                        Log.d("AuthViewModel", "Facebook Login Success")
                    }
                    is LoginResponse.Error -> {
                        _authUiState.update { it.copy(error = response.error) }
                        Log.e("AuthViewModel", "Facebook Login Error: ${response.error}")
                    }
                    else -> {}
                }
            }
        }
    }

    fun cleanErrorMessage() {
        _authUiState.update { it.copy(error = null) }
    }
}