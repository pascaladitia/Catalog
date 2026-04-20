package com.pascal.catalog.ui.screen.profile.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalProfileEvent = compositionLocalOf { ProfileEvent() }

@Stable
data class ProfileEvent(
    val onBookmark: () -> Unit = {},
    val onTheme: () -> Unit = {}
)