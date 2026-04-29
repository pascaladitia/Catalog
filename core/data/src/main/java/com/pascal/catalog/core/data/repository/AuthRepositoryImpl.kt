package com.pascal.catalog.core.data.repository

import com.pascal.catalog.core.data.mapper.toDomain
import com.pascal.catalog.core.domain.model.Login
import com.pascal.catalog.core.domain.repository.AuthRepository
import com.pascal.catalog.core.network.api.FakeStoreApi
import com.pascal.catalog.core.network.base.SafeApiCall
import com.pascal.catalog.core.network.model.login.LoginBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
) : AuthRepository, SafeApiCall() {

    override fun login(username: String, password: String): Flow<Login> = flow {
        val response = safeApiCall {
            api.login(LoginBody(username = username, password = password))
        }
        emit(response.toDomain())
    }
}
