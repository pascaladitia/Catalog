package com.pascal.catalog.feature.catalog.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.core.designsystem.component.CatalogGradientHeader
import com.pascal.catalog.core.designsystem.component.EmptyState
import com.pascal.catalog.core.designsystem.component.LoadingPane
import com.pascal.catalog.core.designsystem.component.ProductCard
import com.pascal.catalog.feature.catalog.R

@Composable
fun HomeRoute(
    onOpenDetail: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onQueryChange = viewModel::updateQuery,
        onCategoryChange = viewModel::updateCategory,
        onRefresh = viewModel::refresh,
        onToggleFavorite = viewModel::toggleFavorite,
        onOpenDetail = onOpenDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onQueryChange: (String) -> Unit,
    onCategoryChange: (String?) -> Unit,
    onRefresh: () -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onOpenDetail: (Int) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val offlineFallbackMessage = stringResource(R.string.offline_fallback_message)

    LaunchedEffect(uiState.showOfflineFallback) {
        if (uiState.showOfflineFallback) {
            snackbarHostState.showSnackbar(message = offlineFallbackMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.catalog_title)) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(R.string.action_refresh),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (uiState.isLoading) {
            LoadingPane(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    CatalogGradientHeader(
                        title = stringResource(R.string.hero_title),
                        subtitle = stringResource(R.string.hero_subtitle),
                    )
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.search_label),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        OutlinedTextField(
                            value = uiState.query,
                            onValueChange = onQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.search_placeholder)) },
                            singleLine = true,
                        )
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = stringResource(R.string.categories_label),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChip(
                                    selected = uiState.selectedCategory == null,
                                    onClick = { onCategoryChange(null) },
                                    label = { Text(stringResource(R.string.category_all)) },
                                )
                            }
                            items(uiState.categories) { category ->
                                FilterChip(
                                    selected = uiState.selectedCategory == category,
                                    onClick = { onCategoryChange(category) },
                                    label = { Text(category) },
                                )
                            }
                        }
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.results_count, uiState.products.size),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (uiState.products.isEmpty()) {
                    item {
                        EmptyState(
                            title = stringResource(R.string.empty_products_title),
                            description = stringResource(R.string.empty_products_description),
                        )
                    }
                } else {
                    items(
                        items = uiState.products,
                        key = { it.id },
                    ) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onOpenDetail(product.id) },
                            onFavoriteClick = { onToggleFavorite(product.id) },
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(12.dp).width(1.dp)) }
            }
        }
    }
}
