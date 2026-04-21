package com.pascal.catalog.feature.catalog.presentation.home

import com.pascal.catalog.core.domain.model.Product

data class HomeUiState(
    val query: String = "",
    val selectedCategory: String? = null,
    val categories: List<String> = emptyList(),
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val showOfflineFallback: Boolean = false,
)
