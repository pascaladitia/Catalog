package com.pascal.catalog.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.database.dao.ProductDao
import com.pascal.catalog.core.database.dao.CategoryDao
import com.pascal.catalog.core.database.dao.OrderDao
import com.pascal.catalog.core.database.dao.UserDao
import com.pascal.catalog.core.database.entity.CategoryEntity
import com.pascal.catalog.core.database.entity.OrderEntity
import com.pascal.catalog.core.database.entity.OrderItemEntity
import com.pascal.catalog.core.database.entity.OrderWithItems
import com.pascal.catalog.core.database.entity.ProductEntity
import com.pascal.catalog.core.database.entity.RatingEmbedded
import com.pascal.catalog.core.database.entity.UserEntity
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.network.api.FakeStoreApi
import com.pascal.catalog.core.network.model.CartDto
import com.pascal.catalog.core.network.model.CartProductDto
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.RatingDto
import com.pascal.catalog.core.network.model.AddressDto
import com.pascal.catalog.core.network.model.NameDto
import com.pascal.catalog.core.network.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProductRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `refresh keeps favorite state when replacing cache`() = runTest(dispatcher) {
        val dao = FakeProductDao(
            initialProducts = listOf(
                entity(id = 1, title = "Backpack", isFavorite = true),
            ),
        )
        val categoryDao = FakeCategoryDao()
        val userDao = FakeUserDao()
        val orderDao = FakeOrderDao()
        val api = FakeApi(
            products = listOf(
                ProductDto(
                    id = 1,
                    title = "Backpack",
                    description = "Carry all",
                    category = "bags",
                    imageUrl = "https://example.com/bag.png",
                    price = 20.0,
                    rating = RatingDto(rate = 4.4, count = 10),
                ),
            ),
        )
        val repository = ProductRepositoryImpl(api, dao, categoryDao, userDao, orderDao, dispatcher)

        val result = repository.refreshProducts()

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        repository.observeProducts().test {
            assertThat(awaitItem().first().isFavorite).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `add to cart increases local quantity`() = runTest(dispatcher) {
        val dao = FakeProductDao(initialProducts = listOf(entity(id = 1, title = "Bag", isFavorite = false)))
        val repository = ProductRepositoryImpl(
            FakeApi(products = emptyList()),
            dao,
            FakeCategoryDao(),
            FakeUserDao(),
            FakeOrderDao(),
            dispatcher,
        )

        repository.addToCart(1)

        repository.observeCartProducts().test {
            assertThat(awaitItem().first().cartQuantity).isEqualTo(1)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private class FakeApi(
        private val products: List<ProductDto>,
    ) : FakeStoreApi {
        override suspend fun getProducts(): List<ProductDto> = products
        override suspend fun getProduct(productId: Int): ProductDto = products.first()
        override suspend fun getCategories(): List<String> = listOf("bags")
        override suspend fun getProductsByCategory(category: String): List<ProductDto> = products
        override suspend fun getUsers(): List<UserDto> = listOf(sampleUser())
        override suspend fun getUser(userId: Int): UserDto = sampleUser()
        override suspend fun getOrdersByUser(userId: Int): List<CartDto> = listOf(
            CartDto(
                id = 1,
                userId = userId,
                date = "2024-01-01",
                products = listOf(CartProductDto(productId = 1, quantity = 2)),
            ),
        )
    }

    private class FakeProductDao(initialProducts: List<ProductEntity>) : ProductDao {
        private val state = MutableStateFlow(initialProducts)

        override fun observeProducts(): Flow<List<ProductEntity>> = state
        override fun searchProducts(query: String): Flow<List<ProductEntity>> =
            state.map { products ->
                products.filter { it.title.contains(query, ignoreCase = true) }
            }

        override fun observeFavoriteProducts(): Flow<List<ProductEntity>> =
            state.map { products -> products.filter { it.isFavorite } }

        override fun observeProduct(productId: Int): Flow<ProductEntity?> =
            state.map { products -> products.firstOrNull { it.id == productId } }

        override suspend fun getFavoriteIds(): List<Int> =
            state.value.filter { it.isFavorite }.map { it.id }

        override suspend fun isFavorite(productId: Int): Boolean? =
            state.value.firstOrNull { it.id == productId }?.isFavorite

        override suspend fun upsertProducts(products: List<ProductEntity>) {
            state.value = products
        }

        override suspend fun updateFavorite(productId: Int, isFavorite: Boolean) {
            state.value = state.value.map {
                if (it.id == productId) it.copy(isFavorite = isFavorite) else it
            }
        }

        override fun observeCartProducts(): Flow<List<ProductEntity>> =
            state.map { products -> products.filter { it.cartQuantity > 0 } }

        override fun observeCartCount(): Flow<Int> =
            state.map { products -> products.sumOf { it.cartQuantity } }

        override suspend fun getCartQuantity(productId: Int): Int? =
            state.value.firstOrNull { it.id == productId }?.cartQuantity

        override suspend fun updateCartQuantity(productId: Int, quantity: Int) {
            state.value = state.value.map {
                if (it.id == productId) it.copy(cartQuantity = quantity) else it
            }
        }

        override suspend fun clearCart() {
            state.value = state.value.map { it.copy(cartQuantity = 0) }
        }

        override suspend fun getCartQuantities() =
            state.value.filter { it.cartQuantity > 0 }.map {
                com.pascal.catalog.core.database.dao.ProductCartQuantity(
                    id = it.id,
                    cartQuantity = it.cartQuantity,
                )
            }
    }

    private class FakeCategoryDao : CategoryDao {
        override fun observeCategories(): Flow<List<CategoryEntity>> = emptyFlow()
        override suspend fun upsertCategories(categories: List<CategoryEntity>) = Unit
    }

    private class FakeUserDao : UserDao {
        override fun observeUser(userId: Int): Flow<UserEntity?> = emptyFlow()
        override suspend fun upsertUser(user: UserEntity) = Unit
    }

    private class FakeOrderDao : OrderDao {
        override fun observeOrders(userId: Int): Flow<List<OrderWithItems>> = emptyFlow()
        override suspend fun upsertOrders(orders: List<OrderEntity>) = Unit
        override suspend fun upsertOrderItems(orderItems: List<OrderItemEntity>) = Unit
        override suspend fun deleteOrderItemsForUser(userId: Int) = Unit
        override suspend fun deleteOrdersForUser(userId: Int) = Unit
    }
}

private fun entity(id: Int, title: String, isFavorite: Boolean) = ProductEntity(
    id = id,
    title = title,
    description = "Desc",
    category = "bags",
    imageUrl = "https://example.com/$id.png",
    price = 10.0,
    isFavorite = isFavorite,
    cartQuantity = 0,
    rating = RatingEmbedded(rate = 4.8, count = 12),
)

private fun sampleUser() = UserDto(
    id = 1,
    email = "user@example.com",
    username = "user",
    name = NameDto(firstname = "Jane", lastname = "Doe"),
    address = AddressDto(city = "Jakarta", street = "Main", number = 1, zipcode = "12345"),
    phone = "08123456789",
)
