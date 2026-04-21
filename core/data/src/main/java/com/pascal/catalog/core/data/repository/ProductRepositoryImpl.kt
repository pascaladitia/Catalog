package com.pascal.catalog.core.data.repository

import com.pascal.catalog.core.common.IoDispatcher
import com.pascal.catalog.core.data.mapper.toDomain
import com.pascal.catalog.core.data.mapper.toEntity
import com.pascal.catalog.core.database.dao.ProductDao
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.model.Product
import com.pascal.catalog.core.domain.repository.ProductRepository
import com.pascal.catalog.core.network.api.FakeStoreApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi,
    private val productDao: ProductDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {

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

    override fun observeFavoriteProducts(): Flow<List<Product>> =
        productDao.observeFavoriteProducts().map { entities -> entities.map { it.toDomain() } }

    override fun observeProduct(productId: Int): Flow<Product?> =
        productDao.observeProduct(productId).map { it?.toDomain() }

    override suspend fun refreshProducts(): DataResult<Unit> = withContext(ioDispatcher) {
        runCatching {
            val favoriteIds = productDao.getFavoriteIds().toSet()
            val remoteProducts = api.getProducts()
            val entities = remoteProducts.map { dto ->
                dto.toEntity(isFavorite = dto.id in favoriteIds)
            }
            productDao.upsertProducts(entities)
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

    override suspend fun toggleFavorite(productId: Int): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val currentFavorite = productDao.isFavorite(productId) ?: false
            productDao.updateFavorite(productId = productId, isFavorite = !currentFavorite)
        }
    }
}
