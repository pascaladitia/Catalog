package com.pascal.catalog.feature.catalog.presentation.detail.mapper

import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.feature.catalog.presentation.detail.state.LocalDetailUiState
import javax.inject.Inject

class DetailUiStateMapper @Inject constructor() {
    fun map(
        productId: Int,
        product: Product?,
        products: List<Product>,
    ): LocalDetailUiState = LocalDetailUiState(
        product = product,
        relatedProducts = products.filter { it.id != productId && it.category == product?.category }.take(4),
    )
}
