package com.pascal.catalog.feature.catalog.presentation.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalHomeEvent = compositionLocalOf { HomeEvent() }

@Stable
data class HomeEvent(
    val onSearchChanged: (String) -> Unit = {},
    val onCategorySelected: (String?) -> Unit = {},
    val onRefresh: () -> Unit = {},
    val onToggleFavorite: (Int) -> Unit = {},
    val onAddToCart: (Int) -> Unit = {},
)
