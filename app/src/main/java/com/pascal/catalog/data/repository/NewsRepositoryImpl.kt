package com.pascal.catalog.data.repository

import com.pascal.catalog.data.remote.api.KtorClientApi
import com.pascal.catalog.data.remote.dtos.AdsBannerResponse
import com.pascal.catalog.data.remote.dtos.BreakingNewsResponse
import com.pascal.catalog.data.remote.dtos.CommonSectionResponse
import com.pascal.catalog.data.remote.dtos.LiveReportResponse
import com.pascal.catalog.data.remote.dtos.dashboard.DashboardResponse
import org.koin.core.annotation.Single

@Single
class NewsRepositoryImpl(
    private val api: KtorClientApi
) : NewsRepository {

    override suspend fun dashboard(): DashboardResponse {
        return api.dashboard()
    }

    override suspend fun getAdsBanner(): AdsBannerResponse {
        return api.getAdsBanner()
    }

    override suspend fun getIframeCampaign(): AdsBannerResponse {
        return api.getIframeCampaign()
    }

    override suspend fun getBreakingNews(): BreakingNewsResponse {
        return api.getBreakingNews()
    }

    override suspend fun getHotTopics(): CommonSectionResponse {
        return api.getHotTopics()
    }

    override suspend fun getLiveReport(): LiveReportResponse {
        return api.getLiveReport()
    }

    override suspend fun getAllCommonSections(): List<CommonSectionResponse> {
        return listOfNotNull(
            api.getKabinet(),
            api.getPonAcehSumut(),
            api.getBanjirBekasi(),
            api.getArticles(),
            api.getOpini(),
            api.getBrief(),
            api.getRedaksi(),
            api.getVideo(),
            api.getLiked(),
            api.getLitbang(),
            api.getInvestigasi(),
            api.getPolitikHukum(),
            api.getEkonomi(),
            api.getInternasional(),
            api.getOlahraga(),
            api.getMultimedia(),
            api.getCerpen(),
            api.getMoreArticle(),
            api.getMoreArticle2()
        )
    }
}