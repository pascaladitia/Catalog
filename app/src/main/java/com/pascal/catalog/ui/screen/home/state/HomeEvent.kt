package com.pascal.catalog.ui.screen.home.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import com.pascal.catalog.domain.model.CommonArticle

val LocalHomeEvent = compositionLocalOf { HomeEvent() }

@Stable
data class HomeEvent(
    val onDetail: (CommonArticle?) -> Unit = {},
    val onBookMark: (CommonArticle?, Boolean) -> Unit = { _, _ -> },
    val onAudio: (String?) -> Unit = {},
    val onShare: (String?) -> Unit = {}
)