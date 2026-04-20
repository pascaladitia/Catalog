package com.pascal.catalog.ui.screen.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.CommonSection
import com.pascal.catalog.domain.model.dummyContentArticles
import com.pascal.catalog.ui.component.screenUtils.DynamicAsyncImage
import com.pascal.catalog.ui.screen.home.state.LocalHomeEvent
import com.pascal.catalog.ui.theme.AppTheme

fun LazyListScope.homeHotTopics(
    item: CommonSection? = null
) {
    if (item == null) return

    item {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            VerticalDivider(
                thickness = 4.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier.fillMaxHeight()
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = item.section,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }

    items(item.topics) { item ->
        val event = LocalHomeEvent.current

        HotTopicsItem(
            image = item.image,
            label = item.title,
            onClick = { event.onDetail(item) }
        )
    }

    item {
        HorizontalDivider(
            thickness = 8.dp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun HotTopicsItem(
    modifier: Modifier = Modifier,
    image: String? = null,
    label: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        image?.let {
            DynamicAsyncImage(
                modifier = Modifier
                    .size(60.dp),
                imageUrl = image,
                placeholder = painterResource(R.drawable.img_campaign),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        label?.let {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                text = label,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Icon(
            modifier = Modifier
                .padding(16.dp)
                .size(28.dp),
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHotTopicsPreview() {
    val dummySection = CommonSection(
        headline = "Berita Terkini",
        section = "Nasional",
        category = "Top Stories",
        isExclusive = true,
        moreLink = "Lihat Selengkapnya",
        articles = listOf(dummyContentArticles()),
        topics = listOf(dummyContentArticles())
    )

    AppTheme {
        LazyColumn {
            homeHotTopics(item = dummySection)
        }
    }
}