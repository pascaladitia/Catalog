package com.pascal.catalog.core.domain.usecase.common

import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.model.Rating
import com.pascal.catalog.core.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository(
    private val products: List<Product>,
    ) : ProductRepository {
        override fun observeProducts(): Flow<List<Product>> = flowOf(products)
        override fun searchProducts(query: String): Flow<List<Product>> =
            flowOf(products.filter { it.title.contains(query, ignoreCase = true) })

        override fun observeCategories(): Flow<List<CategorySummary>> = flowOf(emptyList())
        override fun observeFavoriteProducts(): Flow<List<Product>> = flowOf(emptyList())
        override fun observeCartProducts(): Flow<List<Product>> = flowOf(emptyList())
        override fun observeCartCount(): Flow<Int> = flowOf(0)
        override fun observeProduct(productId: Int): Flow<Product?> = flowOf(products.firstOrNull())
        override fun observeCurrentUser(): Flow<CatalogUser?> = flowOf(null)
        override fun observeOrders(): Flow<List<Order>> = flowOf(emptyList())
        override suspend fun refreshProducts(): DataResult<Unit> = DataResult.Success(Unit)
        override suspend fun refreshProduct(productId: Int): DataResult<Unit> = DataResult.Success(Unit)
        override suspend fun refreshCategory(category: String): DataResult<Unit> = DataResult.Success(Unit)
        override suspend fun toggleFavorite(productId: Int): Result<Unit> = Result.success(Unit)
        override suspend fun addToCart(productId: Int): Result<Unit> = Result.success(Unit)
        override suspend fun updateCartQuantity(productId: Int, quantity: Int): Result<Unit> = Result.success(Unit)
        override suspend fun clearCart(): Result<Unit> = Result.success(Unit)
    }

fun sampleProduct(id: Int, title: String) = Product(
    id = id,
    title = title,
    description = "Description",
    category = "Category",
    imageUrl = "https://example.com/$id.png",
    price = 10.0,
    rating = Rating(rate = 4.5, count = 120),
    isFavorite = false,
    cartQuantity = 0,
)
