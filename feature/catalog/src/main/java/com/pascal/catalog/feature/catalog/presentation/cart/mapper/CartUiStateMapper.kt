package com.pascal.catalog.feature.catalog.presentation.cart.mapper

import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.feature.catalog.presentation.cart.state.LocalCartUiState
import javax.inject.Inject

class CartUiStateMapper @Inject constructor() {
    fun map(products: List<Product>): LocalCartUiState = LocalCartUiState(
        products = products,
        itemCount = products.sumOf { it.cartQuantity },
        subtotal = products.sumOf { it.price * it.cartQuantity },
    )
}
