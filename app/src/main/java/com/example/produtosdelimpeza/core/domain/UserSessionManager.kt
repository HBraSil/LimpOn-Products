package com.example.produtosdelimpeza.core.domain

import com.example.produtosdelimpeza.core.domain.model.User
import com.example.produtosdelimpeza.customer.home.domain.UserRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Singleton
class UserSessionManager @Inject constructor(
    userRepository: UserRepository
) {
    val user: StateFlow<User> = userRepository.getUser()
        .map { it ?: User() }
        .stateIn(
            CoroutineScope(SupervisorJob() + Dispatchers.IO),
            SharingStarted.Eagerly,
            User()
        )
}
