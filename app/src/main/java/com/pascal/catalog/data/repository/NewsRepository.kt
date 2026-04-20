package com.pascal.catalog.data.repository

import com.pascal.catalog.data.remote.dtos.AdsBannerResponse
import com.pascal.catalog.data.remote.dtos.BreakingNewsResponse
import com.pascal.catalog.data.remote.dtos.CommonSectionResponse
import com.pascal.catalog.data.remote.dtos.LiveReportResponse
import com.pascal.catalog.data.remote.dtos.dashboard.DashboardResponse

interface NewsRepository {
    suspend fun dashboard() : DashboardResponse
    suspend fun getAdsBanner(): AdsBannerResponse
    suspend fun getIframeCampaign(): AdsBannerResponse
    suspend fun getBreakingNews(): BreakingNewsResponse
    suspend fun getHotTopics(): CommonSectionResponse
    suspend fun getLiveReport(): LiveReportResponse
    suspend fun getAllCommonSections(): List<CommonSectionResponse>
}