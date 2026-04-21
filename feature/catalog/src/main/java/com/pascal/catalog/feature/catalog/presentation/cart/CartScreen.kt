package com.pascal.catalog.feature.catalog.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.core.designsystem.component.CartItemCard
import com.pascal.catalog.core.designsystem.component.EmptyState
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.cart.state.LocalCartEvent
import com.pascal.catalog.feature.catalog.presentation.cart.state.LocalCartUiState
import java.util.Locale

@Composable
fun CartRoute(
    viewModel: CartViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CartScreen(uiState, viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartScreen(
    uiState: LocalCartUiState,
    onEvent: (LocalCartEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.cart_title)) })
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (uiState.products.isEmpty()) {
                item {
                    EmptyState(
                        title = stringResource(R.string.empty_cart_title),
                        description = stringResource(R.string.empty_cart_description),
                    )
                }
            } else {
                items(uiState.products, key = { it.id }) { product ->
                    CartItemCard(
                        product = product,
                        onIncrease = { onEvent(LocalCartEvent.IncreaseQuantity(product.id)) },
                        onDecrease = { onEvent(LocalCartEvent.DecreaseQuantity(product.id)) },
                    )
                }
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(stringResource(R.string.cart_summary_title))
                            Text(stringResource(R.string.cart_item_count, uiState.itemCount))
                            Text(
                                stringResource(
                                    R.string.cart_subtotal,
                                    "$" + String.format(Locale.US, "%.2f", uiState.subtotal),
                                ),
                            )
                            Button(
                                onClick = { onEvent(LocalCartEvent.ClearCart) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.cart_clear_action))
                            }
                        }
                    }
                }
            }
        }
    }
}
