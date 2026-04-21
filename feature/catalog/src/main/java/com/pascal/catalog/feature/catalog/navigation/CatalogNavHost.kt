package com.pascal.catalog.feature.catalog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.detail.DetailRoute
import com.pascal.catalog.feature.catalog.presentation.favorites.FavoritesRoute
import com.pascal.catalog.feature.catalog.presentation.home.HomeRoute

@Composable
fun CatalogNavHost() {
    val navController = rememberNavController()
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
    )
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == CatalogDestination.Home.route || currentRoute == CatalogDestination.Favorites.route) {
                NavigationBar {
                    tabs.forEach { item ->
                        val selected = backStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
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
        NavHost(
            navController = navController,
            startDestination = CatalogDestination.Home.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            composable(CatalogDestination.Home.route) {
                HomeRoute(
                    onOpenDetail = { productId ->
                        navController.navigate(CatalogDestination.Detail.createRoute(productId))
                    },
                )
            }
            composable(CatalogDestination.Favorites.route) {
                FavoritesRoute(
                    onOpenDetail = { productId ->
                        navController.navigate(CatalogDestination.Detail.createRoute(productId))
                    },
                )
            }
            composable(
                route = CatalogDestination.Detail.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType }),
            ) {
                DetailRoute(onBack = navController::navigateUp)
            }
        }
    }
}

private data class BottomDestination(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)
