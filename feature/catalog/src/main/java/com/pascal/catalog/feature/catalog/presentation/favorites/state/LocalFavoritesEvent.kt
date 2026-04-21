package com.pascal.catalog.feature.catalog.presentation.favorites.state

sealed interface LocalFavoritesEvent {
    data class ToggleFavorite(val productId: Int) : LocalFavoritesEvent
    data class AddToCart(val productId: Int) : LocalFavoritesEvent
}
