package com.pascal.catalog.feature.catalog.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.ObserveCurrentUserUseCase
import com.pascal.catalog.core.domain.usecase.ObserveOrdersUseCase
import com.pascal.catalog.core.domain.usecase.RefreshProductsUseCase
import com.pascal.catalog.feature.catalog.presentation.account.mapper.AccountUiStateMapper
import com.pascal.catalog.feature.catalog.presentation.account.state.LocalAccountUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeOrdersUseCase: ObserveOrdersUseCase,
    private val accountUiStateMapper: AccountUiStateMapper,
    private val refreshProductsUseCase: RefreshProductsUseCase,
) : ViewModel() {
    val uiState = combine(
        observeCurrentUserUseCase(),
        observeOrdersUseCase(),
    ) { user, orders ->
        accountUiStateMapper.map(user, orders)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LocalAccountUiState(),
    )

    fun onRefresh() {
        viewModelScope.launch {
            refreshProductsUseCase()
        }
    }
}
