package com.example.produtosdelimpeza.core.auth.presentation.login

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.auth.data.LoginResponse
import com.example.produtosdelimpeza.core.auth.domain.AuthRepository
import com.example.produtosdelimpeza.core.auth.domain.validation.LoginValidators
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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


    private val callbackManager = CallbackManager.Factory.create()

    fun loginWithFacebook(
        activity: Activity,
        onSuccess: (LoginResult) -> Unit,
        onError: (FacebookException) -> Unit
    ) {
        val loginManager = LoginManager.getInstance()

        loginManager.registerCallback(
            callbackManager,
            callback = object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("FB_LOGIN_SUCCESS", "sem mensagem")
                    onSuccess(result)
                }
                override fun onCancel() {
                    // opcional
                }
                override fun onError(error: FacebookException) {
                    Log.e("FB_LOGIN_ERROR", error.message ?: "sem mensagem", error)
                    onError(error)
                }
            }
        )

        LoginManager.getInstance().logInWithReadPermissions(
            activity,
            listOf("public_profile", "email")
        )
    }

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

    fun changePasswordVisibility() {
        _passwordHidden.value = !_passwordHidden.value
    }


    fun loginWithEmailAndPassword() {
        _loginUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = authRepository.signInWithEmailAndPassword(loginFormState.email.field, loginFormState.password.field)

            when(result) {
                is LoginResponse.Success -> _loginUiState.update { it.copy(goToHome = true, isLoading = false) }
                is LoginResponse.Error -> _loginUiState.value = LoginUiState(error = result.error)
                else -> {}
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            authRepository.signInWithGoogle().collect { result ->
                when(result) {
                    is LoginResponse.Loading -> {  
                        _loginUiState.update { it.copy(isLoading = true) }
                    }
                    is LoginResponse.Success -> {
                        _loginUiState.update { it.copy(goToHome = true, isLoading = false) }
                    }
                    is LoginResponse.Error -> {
                        _loginUiState.value = LoginUiState(error = result.error)
                    }
                }
            }
        }
    }

    fun cleanErrorMessage() {
        _loginUiState.update { it.copy(error = null) }
    }
}