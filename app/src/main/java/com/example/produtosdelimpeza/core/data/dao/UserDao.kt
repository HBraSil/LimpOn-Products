package com.example.produtosdelimpeza.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.produtosdelimpeza.core.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE uid = :uid LIMIT 1")
    fun observeUser(uid: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity)

    @Query("UPDATE user SET name = :name WHERE uid = :uid")
    suspend fun updateName(uid: String, name: String)

    @Query("DELETE FROM user")
    suspend fun clearUser()
}