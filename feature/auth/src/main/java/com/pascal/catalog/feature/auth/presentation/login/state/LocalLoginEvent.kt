package com.pascal.catalog.feature.auth.presentation.login.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalLoginEvent = compositionLocalOf { LoginEvent() }

@Stable
data class LoginEvent(
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onTogglePasswordVisibility: () -> Unit = {},
    val onSubmit: () -> Unit = {},
)
