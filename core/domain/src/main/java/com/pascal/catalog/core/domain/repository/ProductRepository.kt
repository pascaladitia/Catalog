package com.pascal.catalog.core.domain.repository

import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    fun observeFavoriteProducts(): Flow<List<Product>>
    fun observeProduct(productId: Int): Flow<Product?>
    suspend fun refreshProducts(): DataResult<Unit>
    suspend fun toggleFavorite(productId: Int): Result<Unit>
}
