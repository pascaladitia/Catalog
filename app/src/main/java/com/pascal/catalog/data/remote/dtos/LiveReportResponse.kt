package com.pascal.catalog.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LiveReportResponse(
    val report_type: String? = null,
    val main_article: CommonArticle? = null,
    val related_articles: List<CommonArticle>? = null,
    val more_reports: MoreReports? = null,
    val featured_articles: List<CommonArticle>? = null
)

@Serializable
data class MoreReports(
    val label: String? = null,
    val count: String? = null
)