package com.pascal.catalog.feature.catalog.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.ClearCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCartProductsUseCase
import com.pascal.catalog.core.domain.usecase.UpdateCartQuantityUseCase
import com.pascal.catalog.feature.catalog.presentation.cart.mapper.CartUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.cart.state.LocalCartEvent
import com.pascal.catalog.feature.catalog.presentation.cart.state.LocalCartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    observeCartProductsUseCase: ObserveCartProductsUseCase,
    private val cartUiStateMapper: CartUiStateMapper,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : ViewModel() {
    val uiState = observeCartProductsUseCase()
        .map(cartUiStateMapper::map)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalCartUiState(),
        )

    fun onEvent(event: LocalCartEvent) {
        when (event) {
            is LocalCartEvent.IncreaseQuantity -> viewModelScope.launch {
                val product = uiState.value.products.firstOrNull { it.id == event.productId } ?: return@launch
                updateCartQuantityUseCase(event.productId, product.cartQuantity + 1)
            }

            is LocalCartEvent.DecreaseQuantity -> viewModelScope.launch {
                val product = uiState.value.products.firstOrNull { it.id == event.productId } ?: return@launch
                updateCartQuantityUseCase(event.productId, product.cartQuantity - 1)
            }

            LocalCartEvent.ClearCart -> viewModelScope.launch { clearCartUseCase() }
        }
    }
}
