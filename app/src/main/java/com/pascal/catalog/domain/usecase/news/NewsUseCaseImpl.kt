package com.pascal.catalog.domain.usecase.news

import com.pascal.catalog.data.repository.NewsRepository
import com.pascal.catalog.domain.mapper.toDomain
import com.pascal.catalog.domain.model.AdsBanner
import com.pascal.catalog.domain.model.BreakingNews
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.Dashboard
import com.pascal.catalog.domain.model.LiveReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class NewsUseCaseImpl(
    private val repository: NewsRepository
) : NewsUseCase {

    override suspend fun dashboard(): Flow<Dashboard> = flow {
        emit(repository.dashboard().toDomain())
    }

    override suspend fun getAdsBanner(): Flow<AdsBanner> = flow {
        emit(repository.getAdsBanner().toDomain())
    }

    override suspend fun getIframeCampaign(): Flow<AdsBanner> = flow {
        emit(repository.getIframeCampaign().toDomain())
    }

    override suspend fun getBreakingNews(): Flow<BreakingNews> = flow {
        emit(repository.getBreakingNews().toDomain())
    }

    override suspend fun getHotTopics(): Flow<CommonSection> = flow {
        emit(repository.getHotTopics().toDomain())
    }

    override suspend fun getLiveReport(): Flow<LiveReport> = flow {
        emit(repository.getLiveReport().toDomain())
    }

    override suspend fun getAllCommonSections(): Flow<List<CommonSection>> = flow {
        val sections = repository.getAllCommonSections().map { it.toDomain() }
        emit(sections)
    }
}
