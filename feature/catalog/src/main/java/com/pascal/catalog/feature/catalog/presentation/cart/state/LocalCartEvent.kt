package com.pascal.catalog.feature.catalog.presentation.cart.state

sealed interface LocalCartEvent {
    data class IncreaseQuantity(val productId: Int) : LocalCartEvent
    data class DecreaseQuantity(val productId: Int) : LocalCartEvent
    data object ClearCart : LocalCartEvent
}
