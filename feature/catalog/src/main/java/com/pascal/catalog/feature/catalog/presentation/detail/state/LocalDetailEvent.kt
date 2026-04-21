package com.pascal.catalog.feature.catalog.presentation.detail.state

sealed interface LocalDetailEvent {
    data object Refresh : LocalDetailEvent
    data object ToggleFavorite : LocalDetailEvent
    data object AddToCart : LocalDetailEvent
}
