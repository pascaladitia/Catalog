package com.pascal.catalog.feature.catalog.presentation.favorites.mapper

import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.feature.catalog.presentation.favorites.state.LocalFavoritesUiState
import javax.inject.Inject

class FavoritesUiStateMapper @Inject constructor() {
    fun map(products: List<Product>): LocalFavoritesUiState = LocalFavoritesUiState(products = products)
}
