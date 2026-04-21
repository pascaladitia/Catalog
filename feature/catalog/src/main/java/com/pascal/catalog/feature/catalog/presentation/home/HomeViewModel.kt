package com.pascal.catalog.feature.catalog.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.model.DataResult
import com.pascal.catalog.core.domain.usecase.RefreshProductsUseCase
import com.pascal.catalog.core.domain.usecase.SearchProductsUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    searchProductsUseCase: SearchProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val metaState = MutableStateFlow(HomeUiState())
    private val selectedCategory = MutableStateFlow<String?>(null)
    private val productsFlow = query
        .debounce(250)
        .flatMapLatest { searchProductsUseCase(it) }

    val uiState = combine(metaState, productsFlow, selectedCategory) { meta, products, category ->
        val categories = products.map { it.category }.distinct().sorted()
        val visibleProducts = if (category.isNullOrBlank()) {
            products
        } else {
            products.filter { it.category.equals(category, ignoreCase = true) }
        }
        meta.copy(
            query = query.value,
            selectedCategory = category,
            categories = categories,
            products = visibleProducts,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    init {
        refresh()
    }

    fun updateQuery(value: String) {
        query.value = value
        metaState.update { it.copy(query = value) }
    }

    fun updateCategory(category: String?) {
        selectedCategory.value = category
    }

    fun refresh() {
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

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }
}
