package com.pascal.catalog.domain.model

data class CommonSection(
    val headline: String?,
    val section: String,
    val category: String?,
    val isExclusive: Boolean,
    val moreLink: String?,
    val articles: List<CommonArticle>,
    val topics: List<CommonArticle>
)