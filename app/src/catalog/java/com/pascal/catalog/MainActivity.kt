package com.pascal.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pascal.catalog.core.designsystem.theme.CatalogTheme
import com.pascal.catalog.feature.auth.navigation.AuthDestination
import com.pascal.catalog.feature.auth.navigation.authNavGraph
import com.pascal.catalog.feature.catalog.navigation.CatalogScaffold
import com.pascal.catalog.feature.catalog.navigation.catalogNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatalogTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    CatalogScaffold(navController = navController) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = AuthDestination.Login.route,
                            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        ) {
                            authNavGraph(navController = navController)
                            catalogNavGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
