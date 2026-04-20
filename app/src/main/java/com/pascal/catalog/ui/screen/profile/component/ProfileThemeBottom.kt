package com.pascal.catalog.ui.screen.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pascal.catalog.ui.screen.profile.ThemeMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileThemeBottom(
    initialSelection: ThemeMode = ThemeMode.SYSTEM,
    onApply: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var selectedOption by remember { mutableStateOf(initialSelection) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Atur Tema Aplikasi",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pilih tema aplikasi sesuai preferensi Anda",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedOption = ThemeMode.LIGHT }
                ) {
                    RadioButton(
                        selected = selectedOption == ThemeMode.LIGHT,
                        onClick = { selectedOption = ThemeMode.LIGHT }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Selalu dengan tema terang",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedOption = ThemeMode.DARK }
                ) {
                    RadioButton(
                        selected = selectedOption == ThemeMode.DARK,
                        onClick = { selectedOption = ThemeMode.DARK }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Selalu dengan tema gelap",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedOption = ThemeMode.SYSTEM }
                ) {
                    RadioButton(
                        selected = selectedOption == ThemeMode.SYSTEM,
                        onClick = { selectedOption = ThemeMode.SYSTEM }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Sama dengan tema perangkat",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    onApply(selectedOption)
                    scope.launch { sheetState.hide() }
                }
            ) {
                Text(
                    text = "Terapkan",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White
                    )
                )
            }
        }
    }
}

