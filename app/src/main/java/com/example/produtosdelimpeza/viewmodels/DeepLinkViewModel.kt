package com.example.produtosdelimpeza.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.produtosdelimpeza.core.auth.data.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeepLinkViewModel @Inject constructor(
    private val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {
    private val _receivedLink = MutableStateFlow<String?>(null)
    val receivedLink: StateFlow<String?> = _receivedLink.asStateFlow()

    fun onDeepLinkReceived(link: String) {
        Log.d("DEEP_LINK", "ViewModel recebeu link: $link. Iniciando verificação...")

        // Inicia o trabalho assíncrono no escopo do ViewModel
        viewModelScope.launch {

            val result = authRepositoryImpl.processEmailVerificationDeepLink(link)

            result.onSuccess {
                // 1. SUCESSO na verificação do Firebase.
                Log.d("DEEP_LINK", "E-mail verificado com sucesso no Firebase.")
                // 2. Sinaliza para o Composable DeepLinkObserver navegar.
                _receivedLink.value = link

            }.onFailure { e ->
                // FALHA: Link inválido, expirado, ou erro de conexão.
                Log.e("DEEP_LINK", "Erro ao processar Deep Link: ${e.message}")
                // Aqui você pode adicionar lógica para feedback ao usuário (ex: Toast, snackbar)
            }
        }
    }

    fun consume() {
        _receivedLink.value = null
    }
}
