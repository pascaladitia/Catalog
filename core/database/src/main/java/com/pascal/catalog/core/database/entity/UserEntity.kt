package com.pascal.catalog.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val city: String,
    val street: String,
    val zipCode: String,
    val communitySize: Int,
)
