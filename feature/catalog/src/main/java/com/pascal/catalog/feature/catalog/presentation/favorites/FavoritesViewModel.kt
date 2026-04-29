package com.pascal.catalog.feature.catalog.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.AddToCartUseCase
import com.pascal.catalog.core.domain.usecase.ObserveFavoriteProductsUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import com.pascal.catalog.feature.catalog.presentation.favorites.mapper.FavoritesUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.favorites.state.LocalFavoritesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    observeFavoriteProductsUseCase: ObserveFavoriteProductsUseCase,
    private val favoritesUiStateMapper: FavoritesUiStateMapper,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {
    val uiState = observeFavoriteProductsUseCase()
        .map(favoritesUiStateMapper::map)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalFavoritesUiState(),
        )

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
