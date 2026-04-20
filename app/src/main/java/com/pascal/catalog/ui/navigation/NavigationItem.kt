package com.pascal.catalog.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val screen: Screen
)
