@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.pascal.catalog.ui.screen.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.model.dummyContentArticles
import com.pascal.catalog.ui.component.dialog.ShowDialog
import com.pascal.catalog.ui.component.screenUtils.LoadingScreen
import com.pascal.catalog.ui.component.screenUtils.TopAppBarComponent
import com.pascal.catalog.ui.screen.detail.component.DetailThumbnail
import com.pascal.catalog.ui.screen.detail.event.DetailUIState
import com.pascal.catalog.ui.screen.detail.event.LocalDetailEvent
import com.pascal.catalog.ui.theme.AppTheme
import com.pascal.catalog.utils.actionShareUrl
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DetailViewModel = koinViewModel(),
    item: CommonArticle? = null,
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val event = LocalDetailEvent.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setTransition(sharedTransitionScope, animatedVisibilityScope)
        viewModel.setDetailArticle(item)
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
        LocalDetailEvent provides event.copy(
            onNavBack = onNavBack,
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
        DetailContent(uiState = uiState)
    }
}

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: DetailUIState = DetailUIState()
) {
    if (uiState.articles == null) return

    val event = LocalDetailEvent.current
    var moreExpanded by rememberSaveable { mutableStateOf(false) }
    var isFavorite by rememberSaveable { mutableStateOf(uiState.articles.isFavorite) }
    val iconFavorite = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.TopEnd
        ) {
            TopAppBarComponent(
                leftIcon1 = Icons.Default.Close,
                rightIcon1 = Icons.Default.FontDownload,
                rightIcon2 = Icons.AutoMirrored.Filled.VolumeUp,
                rightIcon3 = Icons.Default.MoreVert,
                logoRes = null,
                title = uiState.articles?.label,
                onLeftIcon1Click = { event.onNavBack() },
                onRightIcon1Click = {},
                onRightIcon2Click = { event.onAudio(uiState.articles?.audio) },
                onRightIcon3Click = { moreExpanded = !moreExpanded }
            )

            DropdownMenu(
                expanded = moreExpanded,
                onDismissRequest = { moreExpanded = false },
                offset = DpOffset(x = (-8).dp, y = 0.dp),
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "Bagikan",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    onClick = {
                        moreExpanded = false
                        event.onShare(uiState.articles?.share)
                    }
                )

                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = iconFavorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "Baca Nanti",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    onClick = {
                        moreExpanded = false
                        event.onBookMark(uiState.articles, isFavorite)
                        isFavorite = !isFavorite
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            item {
                DetailThumbnail(
                    uiState = uiState,
                    onIconClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(1)
                        }
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    uiState.articles.author?.let { authorName ->
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = buildAnnotatedString {
                                append("Oleh ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(authorName)
                                }
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    uiState.articles.publishedTime?.let {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 24.dp),
                        text = stringResource(R.string.lorem_ipsum),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailPreview() {
    AppTheme {
        DetailContent(
            uiState = DetailUIState(
                articles = dummyContentArticles()
            )
        )
    }
}