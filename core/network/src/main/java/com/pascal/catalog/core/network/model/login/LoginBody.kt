package com.pascal.catalog.core.network.model.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val username: String,
    val password: String
)