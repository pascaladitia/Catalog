package com.pascal.catalog.domain.mapper

import com.pascal.catalog.data.local.entity.FavoritesEntity
import com.pascal.catalog.data.remote.dtos.AdsBannerResponse
import com.pascal.catalog.data.remote.dtos.BreakingNewsResponse
import com.pascal.catalog.data.remote.dtos.CommonSectionResponse
import com.pascal.catalog.data.remote.dtos.LiveReportResponse
import com.pascal.catalog.data.remote.dtos.dashboard.DashboardResponse
import com.pascal.catalog.domain.model.AdsBanner
import com.pascal.catalog.domain.model.BreakingNews
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.Dashboard
import com.pascal.catalog.domain.model.LiveReport
import com.pascal.catalog.domain.model.MoreReports
import com.pascal.catalog.utils.addRandomParam
import com.pascal.catalog.utils.getRandomAudioUrl
import com.pascal.catalog.utils.getSampleShareUrl

fun DashboardResponse.toDomain(): Dashboard {
    return Dashboard(
        code = this.code ?: 0,
        success = this.success ?: false,
        message = this.message.orEmpty()
    )
}

fun AdsBannerResponse.toDomain() = AdsBanner(
    url = url.orEmpty()
)

fun BreakingNewsResponse.toDomain() = BreakingNews(
    image = image.orEmpty(),
    headline = headline.orEmpty(),
    subheadline = subheadline,
    publishedTime = published_time,
    source = source,
    articles = articles?.map {
        CommonArticle(
            isExclusive = it.isExclusive ?: false,
            image = it.image?.let { url -> addRandomParam(url) },
            title = it.title.orEmpty(),
            label = it.label,
            description = it.description,
            author = it.author,
            category = it.category,
            imageDescription = it.image_description,
            mediaCount = it.media_count,
            publishedTime = it.published_time,
            audio = getRandomAudioUrl(),
            share = "${getSampleShareUrl()}${it.title}"
        )
    }.orEmpty()
)

fun CommonSectionResponse.toDomain(): CommonSection {
    return CommonSection(
        headline = this.headline,
        section = this.section.orEmpty(),
        category = this.category,
        isExclusive = this.isExclusive ?: false,
        moreLink = this.more_link,
        articles = this.articles?.map {
            CommonArticle(
                isExclusive = it.isExclusive ?: false,
                image = it.image?.let { url -> addRandomParam(url) },
                title = it.title.orEmpty(),
                label = it.label,
                description = it.description,
                author = it.author,
                category = it.category,
                imageDescription = it.image_description,
                mediaCount = it.media_count,
                publishedTime = it.published_time,
                audio = getRandomAudioUrl(),
                share = "${getSampleShareUrl()}${it.title}"
            )
        }.orEmpty(),
        topics = this.topics?.map {
            CommonArticle(
                isExclusive = it.isExclusive ?: false,
                image = it.image?.let { url -> addRandomParam(url) },
                title = it.title.orEmpty(),
                label = it.label,
                description = it.description,
                author = it.author,
                category = it.category,
                imageDescription = it.image_description,
                mediaCount = it.media_count,
                publishedTime = it.published_time,
                audio = getRandomAudioUrl(),
                share = "${getSampleShareUrl()}${it.title}"
            )
        }.orEmpty()
    )
}

fun LiveReportResponse.toDomain() = LiveReport(
    reportType = report_type.orEmpty(),
    mainArticle = main_article?.let {
        CommonArticle(
            isExclusive = it.isExclusive ?: false,
            image = it.image?.let { url -> addRandomParam(url) },
            title = it.title.orEmpty(),
            label = it.label,
            description = it.description,
            author = it.author,
            category = it.category,
            imageDescription = it.image_description,
            mediaCount = it.media_count,
            publishedTime = it.published_time,
            audio = getRandomAudioUrl(),
            share = "${getSampleShareUrl()}${it.title}"
        )
    },
    relatedArticles = related_articles?.map {
        CommonArticle(
            isExclusive = it.isExclusive ?: false,
            image = it.image?.let { url -> addRandomParam(url) },
            title = it.title.orEmpty(),
            label = it.label,
            description = it.description,
            author = it.author,
            category = it.category,
            imageDescription = it.image_description,
            mediaCount = it.media_count,
            publishedTime = it.published_time,
            audio = getRandomAudioUrl(),
            share = "${getSampleShareUrl()}${it.title}"
        )
    }.orEmpty(),
    moreReports = more_reports?.let {
        MoreReports(
            label = it.label,
            count = it.count
        )
    },
    featuredArticles = featured_articles?.map {
        CommonArticle(
            isExclusive = it.isExclusive ?: false,
            image = it.image?.let { url -> addRandomParam(url) },
            title = it.title.orEmpty(),
            label = it.label,
            description = it.description,
            author = it.author,
            category = it.category,
            imageDescription = it.image_description,
            mediaCount = it.media_count,
            publishedTime = it.published_time,
            audio = getRandomAudioUrl(),
            share = "${getSampleShareUrl()}${it.title}"
        )
    }.orEmpty()
)

fun BreakingNews.toCommonArticle(): CommonArticle {
    return CommonArticle(
        isExclusive = false,
        image = image,
        title = headline,
        label = source,
        description = subheadline,
        author = source,
        category = "Breaking News",
        imageDescription = null,
        mediaCount = articles.size,
        publishedTime = publishedTime,
        audio = getRandomAudioUrl(),
        share = "${getSampleShareUrl()}${headline}"
    )
}

fun FavoritesEntity.toCommonArticle(): CommonArticle {
    return CommonArticle(
        isExclusive = this.isExclusive ?: false,
        image = this.image,
        title = this.title,
        label = this.label,
        description = this.description,
        author = this.author,
        category = this.category,
        imageDescription = this.imageDescription,
        mediaCount = this.mediaCount,
        publishedTime = this.publishedTime,
        audio = this.audio,
        share = this.share,
        isFavorite = true
    )
}

fun CommonArticle.toFavoritesEntity(): FavoritesEntity {
    return FavoritesEntity(
        isExclusive = this.isExclusive,
        image = this.image,
        title = this.title,
        label = this.label,
        description = this.description,
        author = this.author,
        category = this.category,
        imageDescription = this.imageDescription,
        mediaCount = this.mediaCount,
        publishedTime = this.publishedTime,
        audio = this.audio,
        share = this.share
    )
}


