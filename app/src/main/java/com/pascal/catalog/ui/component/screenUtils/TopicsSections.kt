package com.pascal.catalog.ui.component.screenUtils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pascal.catalog.R
import com.pascal.catalog.ui.theme.AppTheme

@Composable
fun TopicsSection(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    onShareClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        value?.let {
            Spacer(Modifier.width(8.dp))

            TextBorderComponent(
                text = value,
                paddingValues = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onShareClick() }
                .size(42.dp),
            painter = painterResource(R.drawable.ic_share),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicsSectionPreview() {
    AppTheme {
        TopicsSection(
            label = "lorem ipsum dolor sit amet",
            value = "5+"
        )
    }
}