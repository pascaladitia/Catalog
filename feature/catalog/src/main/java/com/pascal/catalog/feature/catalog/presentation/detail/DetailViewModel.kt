package com.pascal.catalog.feature.catalog.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.ObserveProductUseCase
import com.pascal.catalog.core.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeProductUseCase: ObserveProductUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle.get<Int>("productId"))

    val uiState = observeProductUseCase(productId)
        .map { DetailUiState(product = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState(),
        )

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }
}
