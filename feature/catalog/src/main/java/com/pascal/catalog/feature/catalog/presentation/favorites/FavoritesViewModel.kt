package com.pascal.catalog.feature.catalog.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.ObserveFavoriteProductsUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    observeFavoriteProductsUseCase: ObserveFavoriteProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    val uiState = observeFavoriteProductsUseCase()
        .map { FavoritesUiState(products = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesUiState(),
        )

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }
}
