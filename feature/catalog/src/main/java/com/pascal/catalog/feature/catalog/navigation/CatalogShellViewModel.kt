package com.pascal.catalog.feature.catalog.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.ObserveCartCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CatalogShellViewModel @Inject constructor(
    observeCartCountUseCase: ObserveCartCountUseCase,
) : ViewModel() {
    val cartCount = observeCartCountUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0,
    )
}
