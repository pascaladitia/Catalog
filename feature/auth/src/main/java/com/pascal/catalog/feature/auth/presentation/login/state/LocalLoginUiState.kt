package com.pascal.catalog.feature.auth.presentation.login.state

data class LocalLoginUiState(
    val isLoading: Boolean = true,
    val error: Pair<Boolean, String> = false to "",

    val email: Pair<Boolean, String> = false to "",
    val password: Pair<Boolean, String> = false to "",
    val passwordVisibility: Boolean =  false,
    val isLoginSuccess: Boolean = false
)
