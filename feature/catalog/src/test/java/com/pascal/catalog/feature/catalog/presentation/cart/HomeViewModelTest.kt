package com.pascal.catalog.feature.catalog.presentation.cart

import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.domain.usecase.AddToCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCartCountUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCategoriesUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCurrentUserUseCase
import com.pascal.catalog.core.domain.usecase.RefreshCategoryUseCase
import com.pascal.catalog.core.domain.usecase.RefreshProductsUseCase
import com.pascal.catalog.core.domain.usecase.SearchProductsUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import com.pascal.catalog.feature.catalog.presentation.home.mapper.HomeUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.home.state.LocalHomeEvent
import com.pascal.catalog.feature.catalog.presentation.cart.common.MainDispatcherRule
import com.pascal.catalog.feature.catalog.presentation.cart.common.FakeProductRepository
import com.pascal.catalog.feature.catalog.presentation.cart.common.product
import com.pascal.catalog.feature.catalog.presentation.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `get products returns all items`() = runTest {
        val repository = FakeProductRepository(
            initialProducts = listOf(
                product(id = 1, quantity = 0, price = 10.0),
                product(id = 2, quantity = 0, price = 20.0),
                product(id = 3, quantity = 0, price = 30.0),
            )
        )

        val viewModel = createViewModel(repository)

        backgroundScope.launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        val products = viewModel.uiState.value.dealProducts

        assertThat(products).hasSize(3)
        assertThat(products.map { it.id }).containsExactly(1, 2, 3)
    }

    @Test
    fun `search updates query`() = runTest {
        val repository = FakeProductRepository(
            initialProducts = listOf(
                product(id = 1, quantity = 0, price = 10.0),
                product(id = 2, quantity = 0, price = 20.0),
            )
        )

        val viewModel = createViewModel(repository)

        backgroundScope.launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.onEvent(LocalHomeEvent.SearchChanged("Item 1"))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.query).isEqualTo("Item 1")
    }

    @Test
    fun `category selected triggers refresh category`() = runTest {
        val repository = FakeProductRepository(
            initialProducts = emptyList()
        )

        val viewModel = createViewModel(repository)

        backgroundScope.launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        viewModel.onEvent(LocalHomeEvent.CategorySelected("electronics"))
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.selectedCategory).isEqualTo("electronics")
    }

    private fun createViewModel(repository: FakeProductRepository): HomeViewModel {
        return HomeViewModel(
            searchProductsUseCase = SearchProductsUseCase(repository),
            observeCategoriesUseCase = ObserveCategoriesUseCase(repository),
            observeCurrentUserUseCase = ObserveCurrentUserUseCase(repository),
            observeCartCountUseCase = ObserveCartCountUseCase(repository),
            homeUiStateMapper = HomeUiStateMapper(),
            refreshProductsUseCase = RefreshProductsUseCase(repository),
            refreshCategoryUseCase = RefreshCategoryUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository),
            addToCartUseCase = AddToCartUseCase(repository),
        )
    }
}