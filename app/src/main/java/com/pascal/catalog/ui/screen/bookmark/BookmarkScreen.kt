package com.pascal.catalog.ui.screen.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.model.dummyContentArticles
import com.pascal.catalog.ui.component.dialog.ShowDialog
import com.pascal.catalog.ui.component.screenUtils.DynamicAsyncImage
import com.pascal.catalog.ui.component.screenUtils.LoadingScreen
import com.pascal.catalog.ui.component.screenUtils.TopAppBarComponent
import com.pascal.catalog.ui.screen.bookmark.state.BookmarkUIState
import com.pascal.catalog.ui.screen.bookmark.state.LocalBookmarkEvent
import com.pascal.catalog.ui.theme.AppTheme
import com.pascal.catalog.utils.actionShareUrl
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = koinViewModel(),
    onNavBack: () -> Unit,
    onDetail: (CommonArticle?) -> Unit
) {
    val context = LocalContext.current
    val event = LocalBookmarkEvent.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadFavorite()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            viewModel.releaseAudio()
            lifecycleOwner.lifecycle.removeObserver(observer)
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
        LocalBookmarkEvent provides event.copy(
            onNavBack = onNavBack,
            onDetail = onDetail,
            onDelete = {
                viewModel.deleteFavorite(it)
            },
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
        BookmarkContent(uiState = uiState)
    }
}

@Composable
fun BookmarkContent(
    modifier: Modifier = Modifier,
    uiState: BookmarkUIState = BookmarkUIState()
) {
    val event = LocalBookmarkEvent.current
    var moreExpanded by rememberSaveable { mutableStateOf(false) }

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
                leftIcon1 = Icons.AutoMirrored.Filled.ArrowBack,
                rightIcon3 = Icons.Default.MoreVert,
                logoRes = null,
                title = "Baca Nanti",
                onLeftIcon1Click = { event.onNavBack() },
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
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    text = {
                        Text(
                            text = "Hapus Semua",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    onClick = {
                        moreExpanded = false
                        event.onDelete(uiState.articles)
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(uiState.articles.orEmpty()) { item ->
                BookmarkItem(
                    item = item,
                    onItemClick = { event.onDetail(item)},
                    onBookmarkClick = { event.onBookMark(item, it) },
                    onAudioClick = { event.onAudio(item.audio) },
                    onShareClick = { event.onShare(item.share) }
                )
            }
        }
    }
}

@Composable
fun BookmarkItem(
    modifier: Modifier = Modifier,
    item: CommonArticle? = null,
    onShareClick: () -> Unit = {},
    onBookmarkClick: (Boolean) -> Unit = {},
    onAudioClick: () -> Unit = {},
    onItemClick: () -> Unit = {}
) {
    var isFavorite by rememberSaveable { mutableStateOf(item?.isFavorite ?: false) }
    val iconFavorite = if (isFavorite) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        DynamicAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            imageUrl = item?.image.orEmpty(),
            placeholder = painterResource(R.drawable.no_thumbnail),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            item?.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            item?.description?.let {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    item?.publishedTime?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        )
                    }

                    item?.label?.let {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = it,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onShareClick
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        onBookmarkClick(isFavorite)
                        isFavorite = !isFavorite
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = iconFavorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    onClick = onAudioClick
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        HorizontalDivider(
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkPreview() {
    AppTheme {
        BookmarkContent(
            uiState = BookmarkUIState(
                articles = listOf(dummyContentArticles())
            )
        )
    }
}