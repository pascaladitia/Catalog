package com.pascal.catalog.feature.auth.navigation

sealed class AuthDestination(val route: String) {
    data object Login : AuthDestination("login")
    data object Register : AuthDestination("register")
}
