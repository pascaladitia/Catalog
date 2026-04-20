package com.pascal.catalog.ui.component.screenUtils

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pascal.catalog.ui.theme.AppTheme

@Composable
fun ArticleTimeline(
    modifier: Modifier = Modifier,
    time: String,
    title: String,
    showDot: Boolean = true,
    onDetail: () -> Unit = {}
) {
    val redColor = MaterialTheme.colorScheme.error

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetail() }
    ) {
        val (dot, line, content) = createRefs()

        Box(
            modifier = Modifier
                .size(8.dp)
                .background(redColor, CircleShape)
                .constrainAs(dot) {
                    top.linkTo(content.top, margin = 4.dp)
                    start.linkTo(parent.start)
                }
        )

        if (showDot) {
            Canvas(
                modifier = Modifier
                    .width(1.dp)
                    .constrainAs(line) {
                        top.linkTo(dot.bottom, margin = 4.dp)
                        bottom.linkTo(content.bottom, margin = 4.dp)
                        start.linkTo(dot.start)
                        end.linkTo(dot.end)
                        height = Dimension.fillToConstraints
                    }
            ) {
                val dash = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                drawLine(
                    color = redColor,
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    pathEffect = dash,
                    strokeWidth = size.width
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 12.dp, bottom = 16.dp)
                .constrainAs(content) {
                    top.linkTo(dot.top)
                    start.linkTo(dot.end)
                }
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleTimelinePreview() {
    AppTheme {
        ArticleTimeline(
            time = "5 menit lalu",
            title = "Megawati Bernyanyi dan Berjoget saat Kampanye Ganjar-Mahfud di Semarang"
        )
    }
}
