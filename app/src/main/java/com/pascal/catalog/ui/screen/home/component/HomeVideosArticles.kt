package com.pascal.catalog.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.dummySectionArticles
import com.pascal.catalog.ui.component.screenUtils.ArticleComponent
import com.pascal.catalog.ui.component.screenUtils.ArticleSection
import com.pascal.catalog.ui.component.screenUtils.DynamicAsyncImage
import com.pascal.catalog.ui.screen.home.state.LocalHomeEvent
import com.pascal.catalog.ui.theme.AppTheme

@Composable
fun HomeVideosArticles(
    modifier: Modifier = Modifier,
    item: CommonSection? = null
) {
    if (item == null) return

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.outline
        )

        if (item.section.isNotBlank()) {
            ArticleSection(
                label = item.section,
                isExclusive = item.isExclusive
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(item.articles) {
                VideosArticlesItem(item = it)
            }
        }

        item.moreLink?.let {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                text = item.moreLink,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                textDecoration = TextDecoration.Underline
            )
        }

    }
}

@Composable
fun VideosArticlesItem(
    modifier: Modifier = Modifier,
    item: CommonArticle
) {
    val event = LocalHomeEvent.current

    Column(
        modifier = modifier.width(380.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(200.dp)
        ) {
            DynamicAsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                imageUrl = item.image.orEmpty(),
                placeholder = painterResource(R.drawable.no_thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Black.copy(alpha = 0.7f),
                                Color.Transparent
                            ),
                            startY = Float.POSITIVE_INFINITY,
                            endY = 0f
                        )
                    )
            ) {
                Icon(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(48.dp),
                    imageVector = Icons.Outlined.SmartDisplay,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        ArticleComponent(
            title = item.title,
            desc = item.description,
            time = item.publishedTime,
            isFavorite = item.isFavorite,
            showDivider = false,
            onItemClick = { event.onDetail(item)},
            onBookmarkClick = { event.onBookMark(item, it) },
            onAudioClick = { event.onAudio(item.audio) },
            onShareClick = { event.onShare(item.share) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeVideosArticlesPreview() {
    AppTheme {
        HomeVideosArticles(item = dummySectionArticles())
    }
}
