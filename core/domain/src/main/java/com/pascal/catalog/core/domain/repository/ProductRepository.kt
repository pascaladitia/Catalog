package com.pascal.catalog.core.domain.repository

import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Login
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun login(username: String, password: String) : Flow<Login>
    fun observeProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    fun observeCategories(): Flow<List<CategorySummary>>
    fun observeFavoriteProducts(): Flow<List<Product>>
    fun observeCartProducts(): Flow<List<Product>>
    fun observeCartCount(): Flow<Int>
    fun observeProduct(productId: Int): Flow<Product?>
    fun observeCurrentUser(): Flow<CatalogUser?>
    fun observeOrders(): Flow<List<Order>>
    suspend fun refreshProducts(): DataResult<Unit>
    suspend fun refreshProduct(productId: Int): DataResult<Unit>
    suspend fun refreshCategory(category: String): DataResult<Unit>
    suspend fun toggleFavorite(productId: Int): Result<Unit>
    suspend fun addToCart(productId: Int): Result<Unit>
    suspend fun updateCartQuantity(productId: Int, quantity: Int): Result<Unit>
    suspend fun clearCart(): Result<Unit>
}
