package com.pascal.catalog.ui.screen.detail.event

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import com.pascal.catalog.domain.model.CommonArticle

val LocalDetailEvent = compositionLocalOf { DetailEvent() }

@Stable
data class DetailEvent(
    val onNavBack: () -> Unit = {},
    val onBookMark: (CommonArticle?, Boolean) -> Unit = { _, _ -> },
    val onAudio: (String?) -> Unit = {},
    val onShare: (String?) -> Unit = {}
)