package com.pascal.catalog.ui.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.LiveReport
import com.pascal.catalog.domain.model.MoreReports
import com.pascal.catalog.domain.model.dummyContentArticles
import com.pascal.catalog.ui.component.screenUtils.ArticleComponent
import com.pascal.catalog.ui.component.screenUtils.TopicsSection
import com.pascal.catalog.ui.component.screenUtils.ArticleTimeline
import com.pascal.catalog.ui.component.screenUtils.DynamicAsyncImage
import com.pascal.catalog.ui.component.screenUtils.TextBorderComponent
import com.pascal.catalog.ui.screen.home.state.LocalHomeEvent
import com.pascal.catalog.ui.theme.AppTheme

fun LazyListScope.homeLiveReport(
    modifier: Modifier = Modifier,
    item: LiveReport? = null
) {
    if (item == null) return

    item {
        val event = LocalHomeEvent.current

        Column(
            modifier = modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .clickable { event.onDetail(item.mainArticle) }
        ) {
            Box {
                DynamicAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    imageUrl = item.mainArticle?.image.orEmpty(),
                    placeholder = painterResource(R.drawable.no_thumbnail),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                TextBorderComponent(
                    modifier = Modifier.padding(8.dp),
                    text = item.reportType
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = item.mainArticle?.category.orEmpty(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = item.mainArticle?.title.orEmpty(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = item.mainArticle?.publishedTime.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        }
    }

    itemsIndexed(item.relatedArticles) { index, article ->
        val event = LocalHomeEvent.current

        ArticleTimeline(
            modifier = Modifier.padding(horizontal = 16.dp),
            time = article.publishedTime.orEmpty(),
            title = article.title,
            showDot = index != item.relatedArticles.lastIndex,
            onDetail = { event.onDetail(article) }
        )
    }

    item {
        TopicsSection(
            label = item.moreReports?.label.orEmpty(),
            value = item.moreReports?.count.orEmpty()
        )
    }

    items(item.featuredArticles) { item ->
        val event = LocalHomeEvent.current

        ArticleComponent(
            image = item.image,
            title = item.title,
            isFavorite = item.isFavorite,
            onItemClick = { event.onDetail(item)},
            onBookmarkClick = { event.onBookMark(item, it) },
            onAudioClick = { event.onAudio(item.audio) },
            onShareClick = { event.onShare(item.share) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeLiveReportPreview() {
    val dummyLiveReport = LiveReport(
        reportType = "Live Update",
        mainArticle = dummyContentArticles(),
        relatedArticles = listOf(dummyContentArticles()),
        moreReports = MoreReports("test", "2"),
        featuredArticles = listOf(dummyContentArticles())
    )

    AppTheme {
        LazyColumn {
            homeLiveReport(item = dummyLiveReport)
        }
    }
}