package com.pascal.catalog.feature.catalog.navigation

sealed class CatalogDestination(val route: String) {
    data object Home : CatalogDestination("home")
    data object Favorites : CatalogDestination("favorites")
    data object Cart : CatalogDestination("cart")
    data object Account : CatalogDestination("account")
    data object Detail : CatalogDestination("detail/{productId}") {
        fun createRoute(productId: Int): String = "detail/$productId"
    }
}
