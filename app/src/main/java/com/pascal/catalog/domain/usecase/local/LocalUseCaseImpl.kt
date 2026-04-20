package com.pascal.catalog.domain.usecase.local

import com.pascal.catalog.data.local.repository.LocalRepository
import com.pascal.catalog.domain.mapper.toCommonArticle
import com.pascal.catalog.domain.mapper.toFavoritesEntity
import com.pascal.catalog.domain.model.CommonArticle
import org.koin.core.annotation.Single

@Single
class LocalUseCaseImpl(
    private val repository: LocalRepository,
) : LocalUseCase {

    override suspend fun insertFavorite(entity: CommonArticle) {
        repository.insertFavorite(entity.toFavoritesEntity())
    }

    override suspend fun deleteFavorite(entity: CommonArticle) {
        repository.deleteFavorite(entity.toFavoritesEntity())
    }

    override suspend fun getFavorite(): List<CommonArticle>? {
        return repository.getFavorite()?.map { it.toCommonArticle() }
    }

    override suspend fun getFavorite(title: String): Boolean {
        return repository.getFavorite(title)
    }

    override suspend fun clearFavorite() {
        return repository.clearFavorite()
    }
}