package com.example.produtosdelimpeza.core.data

import com.example.produtosdelimpeza.core.data.dao.UserDao
import com.example.produtosdelimpeza.core.data.mapper.toDomain
import com.example.produtosdelimpeza.core.data.mapper.toEntity
import com.example.produtosdelimpeza.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) {
    fun observeUser(uid: String): Flow<User?> = userDao.observeUser(uid).map { it?.toDomain() }

    suspend fun getUserById(uid: String): User? {
        return userDao.getUserById(uid)?.toDomain()
    }

    suspend fun saveUser(user: User) {
        userDao.insertOrUpdate(user.toEntity())
    }

    suspend fun updateUserName(uid: String, name: String) {
        userDao.updateName(uid, name)
    }

    suspend fun clear() {
        userDao.clearUser()
    }
}
