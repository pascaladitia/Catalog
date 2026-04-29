package com.pascal.catalog.feature.auth.presentation.login.state

data class LocalLoginUiState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val isLoginSuccess: Boolean = false,

    val email: Pair<String, Boolean> = "" to false,
    val password: Pair<String, Boolean> = "" to false,
    val isPasswordVisible: Boolean = false,
)
