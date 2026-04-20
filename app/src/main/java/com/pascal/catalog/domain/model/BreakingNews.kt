package com.pascal.catalog.domain.model

data class BreakingNews(
    val image: String,
    val headline: String,
    val subheadline: String?,
    val publishedTime: String?,
    val source: String?,
    val isFavorite: Boolean = false,
    val articles: List<CommonArticle>
)