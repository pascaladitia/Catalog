package com.pascal.catalog.data.remote.api

import android.content.Context
import com.pascal.catalog.data.remote.client
import com.pascal.catalog.data.remote.dtos.AdsBannerResponse
import com.pascal.catalog.data.remote.dtos.BreakingNewsResponse
import com.pascal.catalog.data.remote.dtos.CommonSectionResponse
import com.pascal.catalog.data.remote.dtos.LiveReportResponse
import com.pascal.catalog.data.remote.dtos.dashboard.DashboardResponse
import com.pascal.catalog.utils.base.JsonReader
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single
class KtorClientApi(private val context: Context) {
    suspend fun dashboard(): DashboardResponse {
        return client.get("http:///dashboard").body()
    }

    suspend fun getAdsBanner(): AdsBannerResponse =
        JsonReader.load(context, "ads_banner.json")

    suspend fun getBreakingNews(): BreakingNewsResponse =
        JsonReader.load(context, "breaking_news.json")

    suspend fun getHotTopics(): CommonSectionResponse =
        JsonReader.load(context, "hot_topics.json")

    suspend fun getIframeCampaign(): AdsBannerResponse =
        JsonReader.load(context, "iframe_campaign.json")

    suspend fun getLiveReport(): LiveReportResponse =
        JsonReader.load(context, "live_report.json")

    suspend fun getKabinet(): CommonSectionResponse =
        JsonReader.load(context, "kabinet.json")

    suspend fun getPonAcehSumut(): CommonSectionResponse =
        JsonReader.load(context, "pon_aceh_sumut.json")

    suspend fun getBanjirBekasi(): CommonSectionResponse =
        JsonReader.load(context, "banjir_bekasi.json")

    suspend fun getArticles(): CommonSectionResponse =
        JsonReader.load(context, "articles.json")

    suspend fun getOpini(): CommonSectionResponse =
        JsonReader.load(context, "opini.json")

    suspend fun getBrief(): CommonSectionResponse =
        JsonReader.load(context, "brief.json")

    suspend fun getRedaksi(): CommonSectionResponse =
        JsonReader.load(context, "redaksi.json")

    suspend fun getVideo(): CommonSectionResponse =
        JsonReader.load(context, "video.json")

    suspend fun getLiked(): CommonSectionResponse =
        JsonReader.load(context, "liked.json")

    suspend fun getLitbang(): CommonSectionResponse =
        JsonReader.load(context, "riset_litbang.json")

    suspend fun getInvestigasi(): CommonSectionResponse =
        JsonReader.load(context, "investigasi.json")

    suspend fun getPolitikHukum(): CommonSectionResponse =
        JsonReader.load(context, "politik_hukum.json")

    suspend fun getEkonomi(): CommonSectionResponse =
        JsonReader.load(context, "ekonomi.json")

    suspend fun getInternasional(): CommonSectionResponse =
        JsonReader.load(context, "internasional.json")

    suspend fun getOlahraga(): CommonSectionResponse =
        JsonReader.load(context, "olahraga.json")

    suspend fun getMultimedia(): CommonSectionResponse =
        JsonReader.load(context, "multimedia.json")

    suspend fun getCerpen(): CommonSectionResponse =
        JsonReader.load(context, "cerpen.json")

    suspend fun getMoreArticle(): CommonSectionResponse =
        JsonReader.load(context, "more_articles.json")

    suspend fun getMoreArticle2(): CommonSectionResponse =
        JsonReader.load(context, "more_article2.json")

}