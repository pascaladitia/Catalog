package com.pascal.catalog.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pascal.catalog.feature.auth.presentation.login.LoginRoute
import com.pascal.catalog.feature.catalog.navigation.CatalogDestination

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
) {
    composable(AuthDestination.Login.route) {
        LoginRoute(
            onRegister = {
                navController.navigate(AuthDestination.Register.route)
            },
            onLoginSuccess = {
                navController.navigate(CatalogDestination.Home.route) {
                    popUpTo(AuthDestination.Login.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
    composable(AuthDestination.Register.route) {

    }
}
