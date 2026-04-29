package com.pascal.catalog.feature.catalog.presentation.favorites.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalFavoritesEvent = compositionLocalOf { FavoritesEvent() }

@Stable
data class FavoritesEvent(
    val onToggleFavorite: (Int) -> Unit = {},
    val onAddToCart: (Int) -> Unit = {},
)
