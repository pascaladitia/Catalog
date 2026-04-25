package com.pascal.catalog.feature.auth.presentation.login.state

sealed interface LocalLoginEvent {
    data class OnEmailChange(val value: String) : LocalLoginEvent
    data class OnPasswordChange(val value: String) : LocalLoginEvent
    data object OnPasswordVisibility : LocalLoginEvent
    data object OnSubmit : LocalLoginEvent
}