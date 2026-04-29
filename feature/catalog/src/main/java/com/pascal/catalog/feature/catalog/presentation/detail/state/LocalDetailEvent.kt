package com.pascal.catalog.feature.catalog.presentation.detail.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalDetailEvent = compositionLocalOf { DetailEvent() }

@Stable
data class DetailEvent(
    val onRefresh: () -> Unit = {},
    val onToggleFavorite: () -> Unit = {},
    val onAddToCart: () -> Unit = {},
)
