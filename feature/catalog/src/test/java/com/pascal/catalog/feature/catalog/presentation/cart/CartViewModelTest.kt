package com.pascal.catalog.feature.catalog.presentation.cart

import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.model.Rating
import com.pascal.catalog.core.domain.repository.ProductRepository
import com.pascal.catalog.core.domain.usecase.ClearCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCartProductsUseCase
import com.pascal.catalog.core.domain.usecase.UpdateCartQuantityUseCase
import com.pascal.catalog.feature.catalog.presentation.cart.mapper.CartUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.cart.state.LocalCartEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `decrease quantity updates subtotal`() = runTest(mainDispatcherRule.dispatcher) {
        val repository = FakeProductRepository(
            initialProducts = listOf(
                product(id = 1, quantity = 2, price = 20.0),
            ),
        )
        val viewModel = CartViewModel(
            observeCartProductsUseCase = ObserveCartProductsUseCase(repository),
            cartUiStateMapper = CartUiStateMapper(),
            updateCartQuantityUseCase = UpdateCartQuantityUseCase(repository),
            clearCartUseCase = ClearCartUseCase(repository),
        )
        backgroundScope.launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.onEvent(LocalCartEvent.DecreaseQuantity(productId = 1))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.itemCount).isEqualTo(1)
        assertThat(viewModel.uiState.value.subtotal).isEqualTo(20.0)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

private class FakeProductRepository(
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

private fun product(
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
