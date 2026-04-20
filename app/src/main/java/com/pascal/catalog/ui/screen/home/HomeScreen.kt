@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.pascal.catalog.ui.screen.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.ui.component.dialog.ShowDialog
import com.pascal.catalog.ui.component.screenUtils.LoadingScreen
import com.pascal.catalog.ui.component.screenUtils.TopAppBarComponent
import com.pascal.catalog.ui.screen.home.component.HomeBanner
import com.pascal.catalog.ui.screen.home.component.HomeBriefArticles
import com.pascal.catalog.ui.screen.home.component.HomeCommonArticles
import com.pascal.catalog.ui.screen.home.component.HomeMultimediaArticles
import com.pascal.catalog.ui.screen.home.component.HomeRowArticles
import com.pascal.catalog.ui.screen.home.component.HomeVideosArticles
import com.pascal.catalog.ui.screen.home.component.HomeVisualArticles
import com.pascal.catalog.ui.screen.home.component.PullRefreshComponent
import com.pascal.catalog.ui.screen.home.component.homeBreakingNews
import com.pascal.catalog.ui.screen.home.component.homeCampaign
import com.pascal.catalog.ui.screen.home.component.homeHotTopics
import com.pascal.catalog.ui.screen.home.component.homeLiveReport
import com.pascal.catalog.ui.screen.home.state.HomeUIState
import com.pascal.catalog.ui.screen.home.state.LocalHomeEvent
import com.pascal.catalog.ui.theme.AppTheme
import com.pascal.catalog.utils.actionShareUrl
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = koinViewModel(),
    onDetail: (CommonArticle?) -> Unit
) {
    val context = LocalContext.current
    val event = LocalHomeEvent.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setTransition(sharedTransitionScope, animatedVisibilityScope)
        viewModel.loadHomePartOne()
        viewModel.loadHomePartTwo()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.releaseAudio()
        }
    }

    if (uiState.isLoading) LoadingScreen()

    if (uiState.error.first) {
        ShowDialog(
            message = uiState.error.second,
            textButton = stringResource(R.string.close)
        ) {
            viewModel.resetError()
        }
    }

    CompositionLocalProvider(
        LocalHomeEvent provides event.copy(
            onDetail = onDetail,
            onAudio = {
                viewModel.playAudioFromUrl(context, it)
            },
            onBookMark = { item, isFav ->
                viewModel.modifyFavorite(item, isFav)
            },
            onShare = {
                actionShareUrl(context, it)
            }
        )
    ) {
        PullRefreshComponent(
            onRefresh = {
                viewModel.loadHomePartOne()
                viewModel.loadHomePartTwo()
            }
        ) {
            HomeContent(uiState = uiState)
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeUIState = HomeUIState()
) {
    val coroutine = rememberCoroutineScope()

    val tabItems = NewsTab.entries
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabItems.size }
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            TopAppBarComponent(
                leftIcon1 = Icons.Default.Menu,
                leftIcon2 = Icons.Default.Search,
                rightIcon2 = Icons.Outlined.Notifications
            )

            ScrollableTabRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color.Green
                    )
                }
            ) {
                tabItems.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutine.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { page ->
            when (tabItems[page]) {
                NewsTab.FIRST -> HomeFirstTab(uiState = uiState)
                NewsTab.NEW -> HomeFirstTab(uiState = uiState)
                NewsTab.CHOICE -> HomeFirstTab(uiState = uiState)
                NewsTab.FREE -> HomeFirstTab(uiState = uiState)
                NewsTab.FAVORITE -> HomeFirstTab(uiState = uiState)
            }
        }
    }
}

@Composable
fun HomeFirstTab(
    modifier: Modifier = Modifier,
    uiState: HomeUIState = HomeUIState()
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        homeBreakingNews(uiState = uiState)

        homeLiveReport(item = uiState.liveReport)

        homeCampaign(item = uiState.iframeCampaign)

        homeHotTopics(item = uiState.hotTopics)

        itemsIndexed(uiState.articleList) { index, item ->
            Column {
                when (item.category) {
                    null, "" -> {
                        HomeCommonArticles(item = item)
                    }
                    Category.BRIEF.title -> {
                        HomeBriefArticles(item = item)
                    }
                    Category.VISUAL.title -> {
                        HomeVisualArticles(item = item)
                    }
                    Category.VIDEO.title -> {
                        HomeVideosArticles(item = item)
                    }
                    Category.ROW.title -> {
                        HomeRowArticles(item = item)
                    }
                    Category.MULTIMEDIA.title -> {
                        HomeMultimediaArticles(item = item)
                    }
                    else -> {
                        HomeCommonArticles(item = item)
                    }
                }

                if (uiState.adsBanner != null) {
                    val position = index + 1

                    val shouldShowBanner = when {
                        position == 2 || position == 4 -> true
                        position > 4 && (position - 4) % 3 == 0 -> true
                        else -> false
                    }

                    if (shouldShowBanner) {
                        HomeBanner(item = uiState.adsBanner)
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    AppTheme {
        HomeContent()
    }
}