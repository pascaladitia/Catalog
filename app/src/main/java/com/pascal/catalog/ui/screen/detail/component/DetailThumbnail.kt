@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.pascal.catalog.ui.screen.detail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pascal.catalog.R
import com.pascal.catalog.domain.model.CommonArticle
import com.pascal.catalog.domain.model.dummyContentArticles
import com.pascal.catalog.ui.component.screenUtils.DynamicAsyncImage
import com.pascal.catalog.ui.component.screenUtils.noRippleClickable
import com.pascal.catalog.ui.screen.detail.event.DetailUIState
import com.pascal.catalog.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun DetailThumbnail(
    modifier: Modifier = Modifier,
    uiState: DetailUIState = DetailUIState(),
    onIconClick: () -> Unit = {}
) {
    val item: CommonArticle? = uiState.articles
    val showBackground = remember { mutableStateOf(false) }
    val showTitle = remember { mutableStateOf(false) }
    val showDescription = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(600.dp)
    ) {
        uiState.sharedTransitionScope?.let {
            with(it) {
                DynamicAsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .sharedElement(
                            state = rememberSharedContentState(item?.title.orEmpty()),
                            animatedVisibilityScope = uiState.animatedVisibilityScope!!
                        ),
                    imageUrl = item?.image.orEmpty(),
                    placeholder = painterResource(R.drawable.no_thumbnail),
                    contentScale = ContentScale.Crop
                )
            }
        }

        LaunchedEffect(Unit) {
            showBackground.value = true
            delay(500)
            showTitle.value = true
            delay(500)
            showDescription.value = true
        }

        AnimatedVisibility(
            visible = showBackground.value,
            modifier = Modifier.fillMaxSize(),
            enter = fadeIn(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Black.copy(0.5f))
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
            ) {
                AnimatedVisibility(
                    visible = showTitle.value,
                    enter = slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(durationMillis = 400)
                    ) + fadeIn(animationSpec = tween(durationMillis = 400))
                ) {
                    item?.title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White
                            )
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showDescription.value,
                    enter = slideInVertically(
                        initialOffsetY = { -20 },
                        animationSpec = tween(durationMillis = 400)
                    ) + fadeIn(animationSpec = tween(durationMillis = 400))
                ) {
                    item?.description?.let {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = it,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }

        val infiniteTransition = rememberInfiniteTransition(label = "arrow-bounce")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "arrowOffset"
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .border(1.dp, Color.White, CircleShape)
                .size(38.dp)
                .noRippleClickable { onIconClick() }
        )

        Icon(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .offset(y = offsetY.dp)
                .size(20.dp)
                .noRippleClickable { onIconClick() },
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailThumbnailPreview() {
    AppTheme {
        DetailThumbnail(
            uiState = DetailUIState(
                articles = dummyContentArticles()
            )
        )
    }
}
