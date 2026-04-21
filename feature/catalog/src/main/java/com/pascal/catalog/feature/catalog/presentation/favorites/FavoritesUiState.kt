package com.pascal.catalog.feature.catalog.presentation.favorites

import com.pascal.catalog.core.domain.model.Product

data class FavoritesUiState(
    val products: List<Product> = emptyList(),
)
