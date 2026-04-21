package com.pascal.catalog.feature.catalog.presentation.cart.state

import com.pascal.catalog.core.domain.model.Product

data class LocalCartUiState(
    val products: List<Product> = emptyList(),
    val itemCount: Int = 0,
    val subtotal: Double = 0.0,
)
