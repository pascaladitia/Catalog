package com.pascal.catalog.feature.catalog.presentation.detail.state

import com.pascal.catalog.core.domain.model.Product

data class LocalDetailUiState(
    val product: Product? = null,
    val relatedProducts: List<Product> = emptyList(),
)
