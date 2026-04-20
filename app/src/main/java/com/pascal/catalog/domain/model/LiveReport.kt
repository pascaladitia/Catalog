package com.pascal.catalog.domain.model

data class LiveReport(
    val reportType: String,
    val mainArticle: CommonArticle?,
    val relatedArticles: List<CommonArticle>,
    val moreReports: MoreReports?,
    val featuredArticles: List<CommonArticle>
)

data class MoreReports(
    val label: String?,
    val count: String?
)
