package com.pascal.catalog.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.R
import com.pascal.catalog.ui.component.dialog.ShowDialog
import com.pascal.catalog.ui.component.screenUtils.LoadingScreen
import com.pascal.catalog.ui.component.screenUtils.TopAppBarComponent
import com.pascal.catalog.ui.screen.profile.component.ProfileHeader
import com.pascal.catalog.ui.screen.profile.component.ProfileThemeBottom
import com.pascal.catalog.ui.screen.profile.state.LocalProfileEvent
import com.pascal.catalog.ui.screen.profile.state.ProfileUIState
import com.pascal.catalog.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onBookMark: () -> Unit
) {
    val event = LocalProfileEvent.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showThemeSheet by rememberSaveable { mutableStateOf(false) }

    if (showThemeSheet) {
        ProfileThemeBottom(
            initialSelection = uiState.themeMode,
            onApply = { selection ->
                showThemeSheet = false
                viewModel.onThemeChanged(selection)
            },
            onDismiss = {
                showThemeSheet = false
            }
        )
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
        LocalProfileEvent provides event.copy(
            onBookmark = onBookMark,
            onTheme = { showThemeSheet = true }
        )
    ) {
        ProfileContent(uiState = uiState)
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUIState = ProfileUIState()
) {
    val event = LocalProfileEvent.current

    Column {
        TopAppBarComponent(
            title = "Akun",
            logoRes = null
        )

        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                ProfileHeader()
            }

            item {
                ProfileMenu(
                    icons = Icons.Default.PersonPin,
                    label = "Kelola Akun",
                    desc = "Lihat dan atur akun, status langganan, serta rubrik pilihan anda.",
                    onClick = {}
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.Default.Bookmark,
                    label = "Baca Nanti",
                    desc = "Daftar artikel yang Anda simpan untuk dibaca nanti.",
                    onClick = event.onBookmark
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.Default.Work,
                    label = "Reward",
                    desc = "Lihat berbagai reward yang dapat anda gunakan.",
                    onClick = {}
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.Default.Settings,
                    label = "Pengaturan",
                    desc = "Atur fitur untuk akun Anda.",
                    onClick = event.onTheme
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.Default.Call,
                    label = "Hubungi Kami",
                    desc = "Sampaikan kendala. kritik. dan saran Anda ke TIm Kompas.id.",
                    onClick = {}
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.Default.QuestionMark,
                    label = "Tanya Jawab",
                    desc = "Temukan jawaban dari pertanyaan Anda seputar Kompas.id.",
                    onClick = {}
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.AutoMirrored.Filled.Announcement,
                    label = "Tentang Aplikasi",
                    desc = "Lihat informasi lengkap tentang aplikasi Kompas.id.",
                    onClick = {}
                )
            }

            item {
                ProfileMenu(
                    icons = Icons.AutoMirrored.Filled.FormatAlignRight,
                    label = "Tentang Harian Kompas",
                    desc = "Lihat profil lengkap Harian Kompas.",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun ProfileMenu(
    modifier: Modifier = Modifier,
    icons: ImageVector,
    label: String,
    desc: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp),
                imageVector = icons,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    AppTheme {
        ProfileContent()
    }
}