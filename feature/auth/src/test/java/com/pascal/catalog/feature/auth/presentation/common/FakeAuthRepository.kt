package com.pascal.catalog.feature.auth.presentation.common

import com.pascal.catalog.core.domain.model.Login
import com.pascal.catalog.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthRepository : AuthRepository {
    override fun login(username: String, password: String): Flow<Login> {
        return flowOf(Login(token = "fake_token"))
    }
}
