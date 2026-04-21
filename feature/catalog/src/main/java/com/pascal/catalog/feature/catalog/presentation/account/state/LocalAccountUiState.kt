package com.pascal.catalog.feature.catalog.presentation.account.state

import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.Order

data class LocalAccountUiState(
    val user: CatalogUser? = null,
    val orders: List<Order> = emptyList(),
)
