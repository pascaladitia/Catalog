package com.pascal.catalog.core.domain.repository

import com.pascal.catalog.core.domain.model.Login
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(username: String, password: String): Flow<Login>
}
