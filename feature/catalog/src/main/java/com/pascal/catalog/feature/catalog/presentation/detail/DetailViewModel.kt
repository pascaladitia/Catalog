package com.pascal.catalog.feature.catalog.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.AddToCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveProductUseCase
import com.pascal.catalog.core.domain.usecase.ObserveProductsUseCase
import com.pascal.catalog.core.domain.usecase.RefreshProductUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import com.pascal.catalog.feature.catalog.presentation.detail.mapper.DetailUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.detail.state.LocalDetailEvent
import com.pascal.catalog.feature.catalog.presentation.detail.state.LocalDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeProductUseCase: ObserveProductUseCase,
    observeProductsUseCase: ObserveProductsUseCase,
    private val detailUiStateMapper: DetailUiStateMapper,
    private val refreshProductUseCase: RefreshProductUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {
    private val productId: Int = checkNotNull(savedStateHandle.get<Int>("productId"))

    val uiState = combine(
        observeProductUseCase(productId),
        observeProductsUseCase(),
    ) { product, products ->
        detailUiStateMapper.map(productId, product, products)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LocalDetailUiState(),
    )

    init {
        onEvent(LocalDetailEvent.Refresh)
    }

    fun onEvent(event: LocalDetailEvent) {
        when (event) {
            LocalDetailEvent.Refresh -> viewModelScope.launch { refreshProductUseCase(productId) }
            LocalDetailEvent.ToggleFavorite -> viewModelScope.launch { toggleFavoriteUseCase(productId) }
            LocalDetailEvent.AddToCart -> viewModelScope.launch { addToCartUseCase(productId) }
        }
    }
}
