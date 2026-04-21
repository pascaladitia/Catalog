package com.pascal.catalog.feature.catalog.presentation.account.state

sealed interface LocalAccountEvent {
    data object Refresh : LocalAccountEvent
}
