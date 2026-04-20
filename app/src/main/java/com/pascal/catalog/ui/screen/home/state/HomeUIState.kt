package com.pascal.catalog.ui.screen.home.state

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import com.pascal.catalog.domain.model.AdsBanner
import com.pascal.catalog.domain.model.BreakingNews
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.LiveReport

@ExperimentalSharedTransitionApi
data class HomeUIState(
    val isLoading: Boolean = false,
    val error: Pair<Boolean, String> = false to "",
    val adsBanner: AdsBanner? = null,
    val iframeCampaign: AdsBanner? = null,
    val articles: CommonSection? = null,
    val breakingNews: BreakingNews? = null,
    val hotTopics: CommonSection? = null,
    val liveReport: LiveReport? = null,
    val articleList: List<CommonSection> = emptyList(),
    val sharedTransitionScope: SharedTransitionScope? = null,
    val animatedVisibilityScope: AnimatedVisibilityScope? = null
)