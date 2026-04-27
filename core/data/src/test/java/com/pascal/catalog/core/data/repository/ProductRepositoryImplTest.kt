package com.pascal.catalog.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.data.repository.common.FakeApi
import com.pascal.catalog.core.data.repository.common.FakeCategoryDao
import com.pascal.catalog.core.data.repository.common.FakeOrderDao
import com.pascal.catalog.core.data.repository.common.FakeProductDao
import com.pascal.catalog.core.data.repository.common.FakeUserDao
import com.pascal.catalog.core.data.repository.common.entity
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.network.model.ProductDto
import com.pascal.catalog.core.network.model.RatingDto
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProductRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `login emits token when credentials are correct`() = runTest(dispatcher) {
        val api = FakeApi(products = emptyList())
        val repository = ProductRepositoryImpl(
            api,
            FakeProductDao(emptyList()),
            FakeCategoryDao(),
            FakeUserDao(),
            FakeOrderDao(),
            dispatcher,
        )

        repository.login("user", "password").test {
            val result = awaitItem()

            assertThat(result.token).isEqualTo("fake_token")

            awaitComplete()
        }
    }

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
        val dao = FakeProductDao(
            initialProducts = listOf(
                entity(
                    id = 1,
                    title = "Bag",
                    isFavorite = false
                )
            )
        )
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
}
