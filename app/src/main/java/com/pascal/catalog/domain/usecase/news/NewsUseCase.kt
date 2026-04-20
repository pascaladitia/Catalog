package com.pascal.catalog.domain.usecase.news

import com.pascal.catalog.domain.model.AdsBanner
import com.pascal.catalog.domain.model.BreakingNews
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.Dashboard
import com.pascal.catalog.domain.model.LiveReport
import kotlinx.coroutines.flow.Flow

interface NewsUseCase {
    suspend fun dashboard(): Flow<Dashboard>
    suspend fun getAdsBanner(): Flow<AdsBanner>
    suspend fun getIframeCampaign(): Flow<AdsBanner>
    suspend fun getBreakingNews(): Flow<BreakingNews>
    suspend fun getHotTopics(): Flow<CommonSection>
    suspend fun getLiveReport(): Flow<LiveReport>
    suspend fun getAllCommonSections(): Flow<List<CommonSection>>
}
