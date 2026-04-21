package com.pascal.catalog.feature.catalog.presentation.home.mapper

import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.feature.catalog.presentation.home.state.LocalHomeUiState
import javax.inject.Inject

class HomeUiStateMapper @Inject constructor() {

    fun map(
        metaState: LocalHomeUiState,
        products: List<Product>,
        categories: List<CategorySummary>,
        user: CatalogUser?,
        cartCount: Int,
    ): LocalHomeUiState {
        val filteredProducts = filterProducts(products, metaState.selectedCategory)
        return metaState.copy(
            categories = categories,
            featuredProducts = filteredProducts.take(6),
            dealProducts = filteredProducts.sortedByDescending { it.rating.rate },
            userFirstName = user?.fullName?.substringBefore(" ").orEmpty(),
            cartCount = cartCount,
            isLoading = false,
        )
    }

    private fun filterProducts(
        products: List<Product>,
        category: String?,
    ): List<Product> = if (category.isNullOrBlank()) {
        products
    } else {
        products.filter { it.category.equals(category, ignoreCase = true) }
    }
}
