package com.pascal.catalog.feature.catalog.presentation.cart

import com.google.common.truth.Truth.assertThat
import com.pascal.catalog.core.domain.usecase.ClearCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCartProductsUseCase
import com.pascal.catalog.core.domain.usecase.UpdateCartQuantityUseCase
import com.pascal.catalog.feature.catalog.presentation.cart.common.FakeProductRepository
import com.pascal.catalog.feature.catalog.presentation.cart.common.MainDispatcherRule
import com.pascal.catalog.feature.catalog.presentation.cart.common.product
import com.pascal.catalog.feature.catalog.presentation.cart.mapper.CartUiStateMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

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

        viewModel.onDecreaseQuantity(productId = 1)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.itemCount).isEqualTo(1)
        assertThat(viewModel.uiState.value.subtotal).isEqualTo(20.0)
    }
}
