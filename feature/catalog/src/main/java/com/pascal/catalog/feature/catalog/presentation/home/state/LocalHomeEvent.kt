package com.pascal.catalog.feature.catalog.presentation.home.state

sealed interface LocalHomeEvent {
    data class SearchChanged(val value: String) : LocalHomeEvent
    data class CategorySelected(val category: String?) : LocalHomeEvent
    data object Refresh : LocalHomeEvent
    data class ToggleFavorite(val productId: Int) : LocalHomeEvent
    data class AddToCart(val productId: Int) : LocalHomeEvent
}
