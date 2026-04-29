package com.pascal.catalog.feature.catalog.presentation.account.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalAccountEvent = compositionLocalOf { AccountEvent() }

@Stable
data class AccountEvent(
    val onRefresh: () -> Unit = {},
)
