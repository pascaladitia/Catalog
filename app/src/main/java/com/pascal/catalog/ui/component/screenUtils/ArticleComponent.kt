package com.pascal.catalog.ui.component.screenUtils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.catalog.R
import com.pascal.catalog.ui.theme.AppTheme

@Composable
fun ArticleComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    desc: String? = null,
    time: String? = null,
    image: String? = null,
    label: String? = null,
    author: String? = null,
    action: Boolean = true,
    isFavorite: Boolean = false,
    isCenter: Boolean = false,
    showDivider: Boolean = true,
    onShareClick: () -> Unit = {},
    onBookmarkClick: (Boolean) -> Unit = {},
    onAudioClick: () -> Unit = {},
    onItemClick: () -> Unit = {}
) {
    var isFavorite by rememberSaveable { mutableStateOf(isFavorite) }
    val iconFavorite = if (isFavorite) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    label?.let {
                        val color = if (label.contains("bebas", true)) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceDim

                        TextBorderComponent(
                            modifier = Modifier.padding(bottom = 8.dp),
                            paddingValues = PaddingValues(6.dp),
                            text = label,
                            textColor = color,
                            color = color.copy(0.2f)
                        )
                    }

                    title?.let {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = if (isCenter) TextAlign.Center else null
                        )
                    }

                    author?.let { authorName ->
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
                    
                    desc?.let {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = desc,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            textAlign = if (isCenter) TextAlign.Center else null
                        )
                    }
                }

                image?.let {
                    DynamicAsyncImage(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .size(100.dp),
                        imageUrl = image,
                        placeholder = painterResource(R.drawable.no_thumbnail),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            if (action) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        time?.let {
                            Text(
                                text = time,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            )
                        }
                    }

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onShareClick() }
                            .size(42.dp),
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onBookmarkClick(isFavorite)
                                isFavorite = !isFavorite
                            }
                            .size(42.dp),
                        painter = painterResource(iconFavorite),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onAudioClick() }
                            .size(42.dp),
                        painter = painterResource(R.drawable.ic_audio),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticlePreview() {
    AppTheme {
        ArticleComponent(
            title = "Lorem ipsum dolor sit amet",
            desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            time = "10:00 AM",
            image = "test"
        )
    }
}