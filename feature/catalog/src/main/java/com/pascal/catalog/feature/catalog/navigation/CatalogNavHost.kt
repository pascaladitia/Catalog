package com.pascal.catalog.feature.catalog.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.account.AccountRoute
import com.pascal.catalog.feature.catalog.presentation.cart.CartRoute
import com.pascal.catalog.feature.catalog.presentation.detail.DetailRoute
import com.pascal.catalog.feature.catalog.presentation.favorites.FavoritesRoute
import com.pascal.catalog.feature.catalog.presentation.home.HomeRoute

@Composable
fun CatalogScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
) {
    val shellViewModel: CatalogShellViewModel = hiltViewModel()
    val cartCount by shellViewModel.cartCount.collectAsStateWithLifecycle()
    val tabs = listOf(
        BottomDestination(
            route = CatalogDestination.Home.route,
            label = stringResource(R.string.nav_catalog),
            icon = { Icon(Icons.Rounded.Storefront, contentDescription = null) },
        ),
        BottomDestination(
            route = CatalogDestination.Favorites.route,
            label = stringResource(R.string.nav_favorites),
            icon = { Icon(Icons.Rounded.FavoriteBorder, contentDescription = null) },
        ),
        BottomDestination(
            route = CatalogDestination.Cart.route,
            label = stringResource(R.string.nav_cart),
            icon = {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge { Text(cartCount.toString()) }
                        }
                    },
                ) {
                    Icon(Icons.Rounded.ShoppingCart, contentDescription = null)
                }
            },
        ),
        BottomDestination(
            route = CatalogDestination.Account.route,
            label = stringResource(R.string.nav_account),
            icon = { Icon(Icons.Rounded.PersonOutline, contentDescription = null) },
        ),
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isBottomBarVisible = currentRoute in tabs.map { it.route }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar {
                    tabs.forEach { item ->
                        val selected = backStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(CatalogDestination.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = item.icon,
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}

fun NavGraphBuilder.catalogNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
) {
    composable(CatalogDestination.Home.route) {
        HomeRoute(onOpenDetail = { navController.navigate(CatalogDestination.Detail.createRoute(it)) })
    }
    composable(CatalogDestination.Favorites.route) {
        FavoritesRoute(onOpenDetail = { navController.navigate(CatalogDestination.Detail.createRoute(it)) })
    }
    composable(CatalogDestination.Cart.route) {
        CartRoute()
    }
    composable(CatalogDestination.Account.route) {
        AccountRoute(
            onLogout = onLogout,
        )
    }
    composable(
        route = CatalogDestination.Detail.route,
        arguments = listOf(navArgument("productId") { type = NavType.IntType }),
    ) {
        DetailRoute(
            onBack = navController::navigateUp,
            onOpenDetail = { navController.navigate(CatalogDestination.Detail.createRoute(it)) },
        )
    }
}

private data class BottomDestination(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)
