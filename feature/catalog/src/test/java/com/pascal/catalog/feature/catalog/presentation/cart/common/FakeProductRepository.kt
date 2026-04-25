package com.pascal.catalog.feature.catalog.presentation.cart.common

import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.model.Rating
import com.pascal.catalog.core.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeProductRepository(
    initialProducts: List<Product>,
) : ProductRepository {
    private val products = MutableStateFlow(initialProducts)

    override fun observeProducts(): Flow<List<Product>> = products
    override fun searchProducts(query: String): Flow<List<Product>> = products
    override fun observeCategories(): Flow<List<CategorySummary>> = flowOf(emptyList())
    override fun observeFavoriteProducts(): Flow<List<Product>> = flowOf(emptyList())
    override fun observeCartProducts(): Flow<List<Product>> = products.map { list -> list.filter { it.cartQuantity > 0 } }
    override fun observeCartCount(): Flow<Int> = products.map { list -> list.sumOf { it.cartQuantity } }
    override fun observeProduct(productId: Int): Flow<Product?> = flowOf(products.value.firstOrNull())
    override fun observeCurrentUser(): Flow<CatalogUser?> = flowOf(null)
    override fun observeOrders(): Flow<List<Order>> = flowOf(emptyList())
    override suspend fun refreshProducts(): DataResult<Unit> = DataResult.Success(Unit)
    override suspend fun refreshProduct(productId: Int): DataResult<Unit> = DataResult.Success(Unit)
    override suspend fun refreshCategory(category: String): DataResult<Unit> = DataResult.Success(Unit)
    override suspend fun toggleFavorite(productId: Int): Result<Unit> = Result.success(Unit)
    override suspend fun addToCart(productId: Int): Result<Unit> = Result.success(Unit)
    override suspend fun updateCartQuantity(productId: Int, quantity: Int): Result<Unit> {
        products.value = products.value.map {
            if (it.id == productId) it.copy(cartQuantity = quantity) else it
        }
        return Result.success(Unit)
    }

    override suspend fun clearCart(): Result<Unit> = Result.success(Unit)
}
 
fun product(
    id: Int,
    quantity: Int,
    price: Double,
) = Product(
    id = id,
    title = "Item $id",
    description = "Description",
    category = "electronics",
    imageUrl = "https://example.com/$id.png",
    price = price,
    rating = Rating(rate = 4.7, count = 20),
    isFavorite = false,
    cartQuantity = quantity,
)
