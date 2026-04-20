package com.pascal.catalog.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class BreakingNewsResponse(
    val image: String? = null,
    val headline: String? = null,
    val subheadline: String? = null,
    val published_time: String? = null,
    val articles: List<CommonArticle>? = null,
    val source: String? = null
)