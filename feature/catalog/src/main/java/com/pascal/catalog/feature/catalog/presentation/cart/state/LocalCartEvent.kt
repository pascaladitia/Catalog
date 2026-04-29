package com.pascal.catalog.feature.catalog.presentation.cart.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf

val LocalCartEvent = compositionLocalOf { CartEvent() }

@Stable
data class CartEvent(
    val onIncreaseQuantity: (Int) -> Unit = {},
    val onDecreaseQuantity: (Int) -> Unit = {},
    val onClearCart: () -> Unit = {},
)
