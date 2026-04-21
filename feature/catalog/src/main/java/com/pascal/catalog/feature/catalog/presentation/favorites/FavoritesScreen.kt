package com.pascal.catalog.feature.catalog.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.pascal.catalog.core.designsystem.component.EmptyState
import com.pascal.catalog.core.designsystem.component.ProductRowCard
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.favorites.state.LocalFavoritesEvent
import com.pascal.catalog.feature.catalog.presentation.favorites.state.LocalFavoritesUiState

@Composable
fun FavoritesRoute(
    onOpenDetail: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesScreen(uiState, viewModel::onEvent, onOpenDetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    uiState: LocalFavoritesUiState,
    onEvent: (LocalFavoritesEvent) -> Unit,
    onOpenDetail: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites_title)) },
            )
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
                        title = stringResource(R.string.empty_favorites_title),
                        description = stringResource(R.string.empty_favorites_description),
                    )
                }
            } else {
                items(uiState.products, key = { it.id }) { product ->
                    ProductRowCard(
                        product = product,
                        onClick = { onOpenDetail(product.id) },
                        onFavoriteClick = { onEvent(LocalFavoritesEvent.ToggleFavorite(product.id)) },
                        onAddToCartClick = { onEvent(LocalFavoritesEvent.AddToCart(product.id)) },
                    )
                }
            }
        }
    }
}
