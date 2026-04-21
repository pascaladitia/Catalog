package com.pascal.catalog.feature.catalog.presentation.favorites.state

import com.pascal.catalog.core.domain.model.Product

data class LocalFavoritesUiState(
    val products: List<Product> = emptyList(),
)
