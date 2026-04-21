package com.pascal.catalog.feature.catalog.presentation.account.mapper

import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.feature.catalog.presentation.account.state.LocalAccountUiState
import javax.inject.Inject

class AccountUiStateMapper @Inject constructor() {
    fun map(
        user: CatalogUser?,
        orders: List<Order>,
    ): LocalAccountUiState = LocalAccountUiState(
        user = user,
        orders = orders,
    )
}
