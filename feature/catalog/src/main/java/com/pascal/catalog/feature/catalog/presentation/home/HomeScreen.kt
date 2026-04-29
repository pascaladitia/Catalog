package com.pascal.catalog.feature.catalog.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.core.designsystem.component.CatalogGradientHeader
import com.pascal.catalog.core.designsystem.component.EmptyState
import com.pascal.catalog.core.designsystem.component.LoadingPane
import com.pascal.catalog.core.designsystem.component.ProductHighlightCard
import com.pascal.catalog.core.designsystem.component.ProductRowCard
import com.pascal.catalog.core.designsystem.component.SectionHeader
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.home.state.HomeEvent
import com.pascal.catalog.feature.catalog.presentation.home.state.LocalHomeEvent
import com.pascal.catalog.feature.catalog.presentation.home.state.LocalHomeUiState

@Composable
fun HomeRoute(
    onOpenDetail: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CompositionLocalProvider(
        LocalHomeEvent provides HomeEvent(
            onSearchChanged = viewModel::onSearchChanged,
            onCategorySelected = viewModel::onCategorySelected,
            onRefresh = viewModel::onRefresh,
            onToggleFavorite = viewModel::onToggleFavorite,
            onAddToCart = viewModel::onAddToCart,
        ),
    ) {
        HomeScreen(
            uiState = uiState,
            onOpenDetail = onOpenDetail,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: LocalHomeUiState,
    onOpenDetail: (Int) -> Unit,
) {
    val event = LocalHomeEvent.current
    val snackbarHostState = remember { SnackbarHostState() }
    val offlineFallbackMessage = stringResource(R.string.offline_fallback_message)

    LaunchedEffect(uiState.showOfflineFallback) {
        if (uiState.showOfflineFallback) {
            snackbarHostState.showSnackbar(offlineFallbackMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.catalog_title)) },
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
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                item {
                    CatalogGradientHeader(
                        title = stringResource(R.string.hero_title, uiState.userFirstName.ifBlank { stringResource(R.string.default_shopper_name) }),
                        subtitle = stringResource(R.string.hero_subtitle, uiState.cartCount),
                    )
                }
                item {
                    OutlinedTextField(
                        value = uiState.query,
                        onValueChange = event.onSearchChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.search_placeholder)) },
                        leadingIcon = {
                            Icon(Icons.Rounded.Search, contentDescription = stringResource(R.string.search_icon))
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                    )
                }
                item {
                    SectionHeader(
                        title = stringResource(R.string.categories_label),
                        subtitle = stringResource(R.string.categories_subtitle),
                    )
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterChip(
                                selected = uiState.selectedCategory == null,
                                onClick = { event.onCategorySelected(null) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.White),
                                label = { Text(stringResource(R.string.category_all)) },
                            )
                        }
                        items(uiState.categories) { category ->
                            FilterChip(
                                selected = uiState.selectedCategory == category.name,
                                onClick = { event.onCategorySelected(category.name) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color.White),
                                label = { Text(stringResource(R.string.category_with_count, category.name, category.itemCount)) },
                            )
                        }
                    }
                }
                item {
                    SectionHeader(
                        title = stringResource(R.string.featured_title),
                        subtitle = stringResource(R.string.featured_subtitle),
                    )
                }
                if (uiState.featuredProducts.isEmpty()) {
                    item {
                        EmptyState(
                            title = stringResource(R.string.empty_products_title),
                            description = stringResource(R.string.empty_products_description),
                        )
                    }
                } else {
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(uiState.featuredProducts, key = { it.id }) { product ->
                                ProductHighlightCard(
                                    product = product,
                                    onClick = { onOpenDetail(product.id) },
                                    onFavoriteClick = { event.onToggleFavorite(product.id) },
                                    onAddToCartClick = { event.onAddToCart(product.id) },
                                    modifier = Modifier.fillParentMaxWidth(0.72f),
                                )
                            }
                        }
                    }
                    item {
                        SectionHeader(
                            title = stringResource(R.string.deals_title),
                            subtitle = stringResource(R.string.deals_subtitle),
                        )
                    }
                    items(uiState.dealProducts, key = { it.id }) { product ->
                        ProductRowCard(
                            product = product,
                            onClick = { onOpenDetail(product.id) },
                            onFavoriteClick = { event.onToggleFavorite(product.id) },
                            onAddToCartClick = { event.onAddToCart(product.id) },
                        )
                    }
                }
            }
        }
    }
}
