package com.pascal.catalog.ui.component.screenUtils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.catalog.ui.theme.AppTheme

@Composable
fun ArticleSection(
    modifier: Modifier = Modifier,
    label: String,
    isExclusive: Boolean = false,
    showIcon: Boolean = true
) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isExclusive) {
            VerticalDivider(
                thickness = 4.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 4.dp, end = 16.dp)
                    .fillMaxHeight()
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        if (showIcon) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .size(28.dp),
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun ArticleSectionPreview() {
    AppTheme {
        ArticleSection(
            label = "Sample Section"
        )
    }
}