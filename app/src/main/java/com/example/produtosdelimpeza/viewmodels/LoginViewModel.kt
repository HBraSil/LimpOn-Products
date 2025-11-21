package com.example.produtosdelimpeza.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.produtosdelimpeza.R
import com.example.produtosdelimpeza.data.LoginRepositoryImpl
import com.example.produtosdelimpeza.model.LoginFormState
import com.example.produtosdelimpeza.validation.LoginValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepositoryImpl
): ViewModel() {

    var loginFormState by mutableStateOf(LoginFormState())
    var passwordHidden by mutableStateOf(true)


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
        passwordHidden = !passwordHidden
    }


    fun login() {
        repository.login(loginFormState.email.field, loginFormState.password.field)
    }
}