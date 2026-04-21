package com.pascal.catalog.feature.catalog.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.core.designsystem.component.CatalogNetworkImage
import com.pascal.catalog.core.designsystem.component.EmptyState
import com.pascal.catalog.core.designsystem.component.ProductHighlightCard
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.detail.state.LocalDetailEvent
import com.pascal.catalog.feature.catalog.presentation.detail.state.LocalDetailUiState
import java.util.Locale

@Composable
fun DetailRoute(
    onBack: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreen(uiState, viewModel::onEvent, onBack, onOpenDetail)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(
    uiState: LocalDetailUiState,
    onEvent: (LocalDetailEvent) -> Unit,
    onBack: () -> Unit,
    onOpenDetail: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                },
            )
        },
    ) { innerPadding ->
        val product = uiState.product
        if (product == null) {
            EmptyState(
                title = stringResource(R.string.detail_not_found_title),
                description = stringResource(R.string.detail_not_found_description),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            CatalogNetworkImage(
                                imageUrl = product.imageUrl,
                                contentDescription = product.title,
                                modifier = Modifier.fillMaxWidth(),
                            )
                            AssistChip(onClick = {}, label = { Text(product.category) })
                            Text(text = product.title, style = MaterialTheme.typography.headlineSmall)
                            Text(
                                text = "$" + String.format(Locale.US, "%.2f", product.price),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(text = product.description, style = MaterialTheme.typography.bodyLarge)
                            androidx.compose.foundation.layout.Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(Icons.Rounded.Star, contentDescription = null)
                                Text(stringResource(R.string.detail_rating, product.rating.rate, product.rating.count))
                            }
                            Button(
                                onClick = { onEvent(LocalDetailEvent.AddToCart) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(stringResource(R.string.action_add_favorite_cart))
                            }
                            Button(
                                onClick = { onEvent(LocalDetailEvent.ToggleFavorite) },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Icon(Icons.Rounded.Favorite, contentDescription = null)
                                Text(
                                    text = if (product.isFavorite) {
                                        stringResource(R.string.action_remove_favorite)
                                    } else {
                                        stringResource(R.string.action_add_favorite)
                                    },
                                    modifier = Modifier.padding(start = 8.dp),
                                )
                            }
                        }
                    }
                }
                if (uiState.relatedProducts.isNotEmpty()) {
                    item { Text(stringResource(R.string.related_products_title)) }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(uiState.relatedProducts, key = { it.id }) { related ->
                                ProductHighlightCard(
                                    product = related,
                                    onClick = { onOpenDetail(related.id) },
                                    onFavoriteClick = { onEvent(LocalDetailEvent.ToggleFavorite) },
                                    onAddToCartClick = { onEvent(LocalDetailEvent.AddToCart) },
                                    modifier = Modifier.fillParentMaxWidth(0.74f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
