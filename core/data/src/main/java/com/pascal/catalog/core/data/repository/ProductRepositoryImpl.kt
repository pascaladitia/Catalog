package com.pascal.catalog.core.data.repository

import com.pascal.catalog.core.common.IoDispatcher
import com.pascal.catalog.core.data.mapper.toDomain
import com.pascal.catalog.core.data.mapper.toEntity
import com.pascal.catalog.core.data.mapper.toCartQuantityMap
import com.pascal.catalog.core.data.mapper.toCategoryEntities
import com.pascal.catalog.core.data.mapper.toOrderEntity
import com.pascal.catalog.core.data.mapper.toOrderItemEntities
import com.pascal.catalog.core.database.dao.ProductDao
import com.pascal.catalog.core.database.dao.CategoryDao
import com.pascal.catalog.core.database.dao.OrderDao
import com.pascal.catalog.core.database.dao.UserDao
import com.pascal.catalog.core.database.entity.ProductEntity
import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.CategorySummary
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Login
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.repository.ProductRepository
import com.pascal.catalog.core.network.api.FakeStoreApi
import com.pascal.catalog.core.network.model.login.LoginBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val userDao: UserDao,
    private val orderDao: OrderDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {

    private val currentUserId = 1

    override fun login(username: String, password: String): Flow<Login> = flow {
        emit(api.login(LoginBody(username, password)).toDomain())
    }

    override fun observeProducts(): Flow<List<Product>> =
        productDao.observeProducts().map { entities -> entities.map { it.toDomain() } }

    override fun searchProducts(query: String): Flow<List<Product>> {
        val source = if (query.isBlank()) {
            productDao.observeProducts()
        } else {
            productDao.searchProducts(query.trim())
        }
        return source.map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeCategories(): Flow<List<CategorySummary>> = combine(
        categoryDao.observeCategories(),
        productDao.observeProducts(),
    ) { categories, products ->
        categories.map { category ->
            category.toDomain(
                itemCount = products.count { it.category.equals(category.name, ignoreCase = true) },
            )
        }
    }

    override fun observeFavoriteProducts(): Flow<List<Product>> =
        productDao.observeFavoriteProducts().map { entities -> entities.map { it.toDomain() } }

    override fun observeCartProducts(): Flow<List<Product>> =
        productDao.observeCartProducts().map { entities -> entities.map { it.toDomain() } }

    override fun observeCartCount(): Flow<Int> = productDao.observeCartCount()

    override fun observeProduct(productId: Int): Flow<Product?> =
        productDao.observeProduct(productId).map { it?.toDomain() }

    override fun observeCurrentUser(): Flow<CatalogUser?> =
        userDao.observeUser(currentUserId).map { it?.toDomain() }

    override fun observeOrders(): Flow<List<Order>> = combine(
        orderDao.observeOrders(currentUserId),
        productDao.observeProducts(),
    ) { orders, products ->
        val productMap = products.associateBy(ProductEntity::id)
        orders.map { it.toDomain(productMap) }
    }

    override suspend fun refreshProducts(): DataResult<Unit> = withContext(ioDispatcher) {
        runCatching {
            coroutineScope {
                val productsDeferred = async { api.getProducts() }
                val categoriesDeferred = async { api.getCategories() }
                val usersDeferred = async { api.getUsers() }
                val currentUserDeferred = async { api.getUser(currentUserId) }
                val ordersDeferred = async { api.getOrdersByUser(currentUserId) }

                val favorites = productDao.getFavoriteIds().toSet()
                val cartQuantities = productDao.getCartQuantities().toCartQuantityMap()
                val remoteProducts = productsDeferred.await()
                val categories = categoriesDeferred.await()
                val users = usersDeferred.await()
                val currentUser = currentUserDeferred.await()
                val orders = ordersDeferred.await()

                val entities = remoteProducts.map { dto ->
                    dto.toEntity(
                        isFavorite = dto.id in favorites,
                        cartQuantity = cartQuantities[dto.id] ?: 0,
                    )
                }
                productDao.upsertProducts(entities)
                categoryDao.upsertCategories(categories.toCategoryEntities())
                userDao.upsertUser(currentUser.toEntity(communitySize = users.size))
                orderDao.deleteOrderItemsForUser(currentUserId)
                orderDao.deleteOrdersForUser(currentUserId)
                orderDao.upsertOrders(orders.map { it.toOrderEntity() })
                orderDao.upsertOrderItems(orders.flatMap { it.toOrderItemEntities() })
            }
        }.fold(
            onSuccess = { DataResult.Success(Unit) },
            onFailure = { throwable ->
                DataResult.Error(
                    message = throwable.message ?: "Unknown error",
                    cause = throwable,
                )
            },
        )
    }

    override suspend fun refreshProduct(productId: Int): DataResult<Unit> = withContext(ioDispatcher) {
        runCatching {
            val favorite = productDao.isFavorite(productId) ?: false
            val cartQuantity = productDao.getCartQuantity(productId) ?: 0
            val product = api.getProduct(productId)
            productDao.upsertProducts(
                listOf(
                    product.toEntity(
                        isFavorite = favorite,
                        cartQuantity = cartQuantity,
                    ),
                ),
            )
        }.fold(
            onSuccess = { DataResult.Success(Unit) },
            onFailure = { DataResult.Error(it.message ?: "Unknown error", it) },
        )
    }

    override suspend fun refreshCategory(category: String): DataResult<Unit> = withContext(ioDispatcher) {
        runCatching {
            val favorites = productDao.getFavoriteIds().toSet()
            val cartQuantities = productDao.getCartQuantities().toCartQuantityMap()
            val products = api.getProductsByCategory(category)
            productDao.upsertProducts(
                products.map { product ->
                    product.toEntity(
                        isFavorite = product.id in favorites,
                        cartQuantity = cartQuantities[product.id] ?: 0,
                    )
                },
            )
        }.fold(
            onSuccess = { DataResult.Success(Unit) },
            onFailure = { DataResult.Error(it.message ?: "Unknown error", it) },
        )
    }

    override suspend fun toggleFavorite(productId: Int): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val currentFavorite = productDao.isFavorite(productId) ?: false
            productDao.updateFavorite(productId = productId, isFavorite = !currentFavorite)
        }
    }

    override suspend fun addToCart(productId: Int): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val current = productDao.getCartQuantity(productId) ?: 0
            productDao.updateCartQuantity(productId, current + 1)
        }
    }

    override suspend fun updateCartQuantity(productId: Int, quantity: Int): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                productDao.updateCartQuantity(productId, quantity.coerceAtLeast(0))
            }
        }

    override suspend fun clearCart(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            productDao.clearCart()
        }
    }
}
