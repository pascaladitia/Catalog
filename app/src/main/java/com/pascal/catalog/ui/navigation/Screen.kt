package com.pascal.catalog.ui.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object HomeScreen: Screen("home")
    data object EPaperScreen: Screen("ePaper")
    data object TTSScreen: Screen("tts")
    data object BookScreen: Screen("book")
    data object ProfileScreen: Screen("profile")
    data object DetailScreen: Screen("detail")
    data object BookmarkScreen: Screen("bookmark")
}