package com.pascal.catalog.domain.mapper

import com.pascal.catalog.domain.model.BreakingNews
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.LiveReport
import com.pascal.catalog.domain.usecase.local.LocalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun LocalUseCase.getFavoriteTitlesFlow(): Flow<Set<String>> = flow {
    emit(getFavorite()?.map { it.title }?.toSet() ?: emptySet())
}

fun CommonArticle.withFavorite(favorites: Boolean) = copy(
    isFavorite = favorites
)

fun CommonSection.withFavorites(favorites: Set<String>) = copy(
    articles = articles.map { it.copy(isFavorite = favorites.contains(it.title)) },
    topics = topics.map { it.copy(isFavorite = favorites.contains(it.title)) }
)

fun BreakingNews.withFavorites(favorites: Set<String>) = copy(
    isFavorite = favorites.contains(headline),
    articles = articles.map { it.copy(isFavorite = favorites.contains(it.title)) }
)

fun LiveReport.withFavorites(favorites: Set<String>) = copy(
    mainArticle = mainArticle?.copy(isFavorite = favorites.contains(mainArticle.title)),
    relatedArticles = relatedArticles.map { it.copy(isFavorite = favorites.contains(it.title)) },
    featuredArticles = featuredArticles.map { it.copy(isFavorite = favorites.contains(it.title)) }
)