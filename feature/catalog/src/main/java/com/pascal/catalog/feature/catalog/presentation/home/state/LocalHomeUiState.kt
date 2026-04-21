package com.pascal.catalog.feature.catalog.presentation.home.state

import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.Product

data class LocalHomeUiState(
    val query: String = "",
    val selectedCategory: String? = null,
    val categories: List<CategorySummary> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val dealProducts: List<Product> = emptyList(),
    val userFirstName: String = "",
    val cartCount: Int = 0,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val showOfflineFallback: Boolean = false,
)
