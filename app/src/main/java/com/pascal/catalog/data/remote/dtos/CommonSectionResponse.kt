package com.pascal.catalog.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CommonSectionResponse(
    val headline: String? = null,
    val section: String? = null,
    val category: String? = null,
    val isExclusive: Boolean? = null,
    val more_link: String? = null,
    val articles: List<CommonArticle>? = null,
    val topics: List<CommonArticle>? = null
)

@Serializable
data class CommonArticle(
    val isExclusive: Boolean? = null,
    val image: String? = null,
    val title: String? = null,
    val label: String? = null,
    val description: String? = null,
    val author: String? = null,
    val category: String? = null,
    val image_description: String? = null,
    val media_count: Int? = null,
    val published_time: String? = null,
    val audio: String? = null,
    val share: String? = null
)
