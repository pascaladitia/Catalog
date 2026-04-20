package com.pascal.catalog.ui.screen.bookmark.state

import com.pascal.catalog.domain.model.CommonArticle

data class BookmarkUIState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val articles: List<CommonArticle>? = null
)