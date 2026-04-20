package com.pascal.catalog.domain.usecase.local

import com.pascal.catalog.domain.model.CommonArticle

interface LocalUseCase {
    suspend fun insertFavorite(entity: CommonArticle)
    suspend fun deleteFavorite(entity: CommonArticle)
    suspend fun getFavorite(): List<CommonArticle>?
    suspend fun getFavorite(title: String): Boolean
    suspend fun clearFavorite()
}