package com.pascal.catalog.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pascal.catalog.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun observeUser(userId: Int): Flow<UserEntity?>

    @Upsert
    suspend fun upsertUser(user: UserEntity)
}
