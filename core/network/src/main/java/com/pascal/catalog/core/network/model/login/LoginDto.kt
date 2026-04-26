package com.pascal.catalog.core.network.model.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val token: String? = null
)