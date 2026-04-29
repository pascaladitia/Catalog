package com.pascal.catalog.feature.catalog.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.usecase.AddToCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCartCountUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCategoriesUseCase
import com.pascal.catalog.core.domain.usecase.ObserveCurrentUserUseCase
import com.pascal.catalog.core.domain.usecase.RefreshCategoryUseCase
import com.pascal.catalog.core.domain.usecase.RefreshProductsUseCase
import com.pascal.catalog.core.domain.usecase.SearchProductsUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import com.pascal.catalog.feature.catalog.presentation.home.mapper.HomeUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.home.state.LocalHomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    searchProductsUseCase: SearchProductsUseCase,
    observeCategoriesUseCase: ObserveCategoriesUseCase,
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeCartCountUseCase: ObserveCartCountUseCase,
    private val homeUiStateMapper: HomeUiStateMapper,
    private val refreshProductsUseCase: RefreshProductsUseCase,
    private val refreshCategoryUseCase: RefreshCategoryUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {

    private val metaState = MutableStateFlow(LocalHomeUiState())
    private val query = MutableStateFlow("")

    private val productsFlow = query
        .debounce(250)
        .flatMapLatest { searchProductsUseCase(it) }

    val uiState = combine(
        metaState,
        productsFlow,
        observeCategoriesUseCase(),
        observeCurrentUserUseCase(),
        observeCartCountUseCase(),
    ) { meta, products, categories, user, cartCount ->
        homeUiStateMapper.map(meta, products, categories, user, cartCount)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LocalHomeUiState(),
    )

    init {
        onRefresh()
    }

    fun onSearchChanged(value: String) {
        query.value = value
        metaState.update { it.copy(query = value) }
    }

    fun onCategorySelected(category: String?) {
        metaState.update { it.copy(selectedCategory = category) }
        if (category == null) return

        viewModelScope.launch {
            refreshCategoryUseCase(category)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            metaState.update { it.copy(isRefreshing = true, showOfflineFallback = false) }
            val result = refreshProductsUseCase()
            metaState.update {
                it.copy(
                    isRefreshing = false,
                    showOfflineFallback = result is DataResult.Error,
                )
            }
        }
    }

    fun onToggleFavorite(productId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }

    fun onAddToCart(productId: Int) {
        viewModelScope.launch {
            addToCartUseCase(productId)
        }
    }
}
