package com.pascal.catalog.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.database.dao.ProductDao
import com.pascal.catalog.core.database.entity.ProductEntity
import com.pascal.catalog.core.database.entity.RatingEmbedded
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.network.api.FakeStoreApi
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.RatingDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
        val repository = ProductRepositoryImpl(api, dao, dispatcher)

        val result = repository.refreshProducts()

        assertThat(result).isInstanceOf(DataResult.Success::class.java)
        repository.observeProducts().test {
            assertThat(awaitItem().first().isFavorite).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    private class FakeApi(
        private val products: List<ProductDto>,
    ) : FakeStoreApi {
        override suspend fun getProducts(): List<ProductDto> = products
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
    rating = RatingEmbedded(rate = 4.8, count = 12),
)
