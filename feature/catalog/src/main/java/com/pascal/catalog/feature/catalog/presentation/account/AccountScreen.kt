package com.pascal.catalog.feature.catalog.presentation.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Groups2
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pascal.catalog.core.designsystem.component.EmptyState
import com.pascal.catalog.core.designsystem.component.SectionHeader
import com.pascal.catalog.core.designsystem.theme.Coral
import com.pascal.catalog.core.designsystem.theme.Forest
import com.pascal.catalog.core.designsystem.theme.Gold
import com.pascal.catalog.core.designsystem.theme.Mist
import com.pascal.catalog.core.designsystem.theme.OceanDark
import com.pascal.catalog.core.domain.model.CatalogUser
import com.pascal.catalog.core.domain.model.Order
import com.pascal.catalog.feature.catalog.R
import com.pascal.catalog.feature.catalog.presentation.account.state.LocalAccountEvent
import com.pascal.catalog.feature.catalog.presentation.account.state.LocalAccountUiState
import java.util.Locale

@Composable
fun AccountRoute(
    viewModel: AccountViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AccountScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onLogout = onLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    uiState: LocalAccountUiState,
    onEvent: (LocalAccountEvent) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.account_title)) })
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                val user = uiState.user
                if (user == null) {
                    EmptyState(
                        title = stringResource(R.string.account_empty_title),
                        description = stringResource(R.string.account_empty_description),
                    )
                } else {
                    ProfileHeroCard(user = user, onLogout = onLogout)
                }
            }
            item {
                SectionHeader(
                    title = stringResource(R.string.account_orders_title),
                    subtitle = stringResource(R.string.account_orders_subtitle, uiState.orders.size),
                )
            }
            if (uiState.orders.isEmpty()) {
                item {
                    EmptyState(
                        title = stringResource(R.string.orders_empty_title),
                        description = stringResource(R.string.orders_empty_description),
                    )
                }
            } else {
                items(uiState.orders, key = { it.id }) { order ->
                    OrderHistoryCard(order = order)
                }
            }
        }
    }
}

@Composable
private fun ProfileHeroCard(
    user: CatalogUser,
    onLogout: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .background(color = OceanDark, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = user.fullName.take(1).uppercase(Locale.getDefault()),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = user.fullName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.account_username, user.username),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                AssistChip(
                    onClick = {},
                    label = { Text(stringResource(R.string.account_member_label)) },
                )
            }

            ProfileInfoRow(
                icon = Icons.Rounded.Email,
                label = stringResource(R.string.account_email_label),
                value = user.email,
            )
            ProfileInfoRow(
                icon = Icons.Rounded.Phone,
                label = stringResource(R.string.account_phone_label),
                value = user.phone,
            )
            ProfileInfoRow(
                icon = Icons.Rounded.LocationOn,
                label = stringResource(R.string.account_address_label),
                value = stringResource(R.string.account_address, user.street, user.city, user.zipCode),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.Groups2,
                    title = stringResource(R.string.account_community_metric),
                    value = user.communitySize.toString(),
                    accentColor = Forest,
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.Person,
                    title = stringResource(R.string.account_city_metric),
                    value = user.city,
                    accentColor = Gold,
                )
            }

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_logout))
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(
            color = Mist,
            shape = CircleShape,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                tint = OceanDark,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    accentColor: androidx.compose.ui.graphics.Color,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(
                color = accentColor.copy(alpha = 0.14f),
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = accentColor,
                )
            }
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun OrderHistoryCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    color = Coral.copy(alpha = 0.14f),
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ReceiptLong,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp),
                        tint = Coral,
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = stringResource(R.string.account_order_id, order.id),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.account_order_date, order.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                AssistChip(
                    onClick = {},
                    label = { Text(stringResource(R.string.account_order_items, order.itemCount)) },
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.AutoMirrored.Rounded.ReceiptLong,
                    title = stringResource(R.string.account_order_total_label),
                    value = "$" + String.format(Locale.US, "%.2f", order.totalAmount),
                    accentColor = OceanDark,
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.Groups2,
                    title = stringResource(R.string.account_order_items_label),
                    value = order.itemCount.toString(),
                    accentColor = Coral,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.account_order_products_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
                order.items.take(4).forEach { line ->
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp),
                            ) {
                                Text(
                                    text = line.productTitle,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                Text(
                                    text = stringResource(R.string.account_order_quantity, line.quantity),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                )
                            }
                            Text(
                                text = "$" + String.format(Locale.US, "%.2f", line.lineTotal),
                                style = MaterialTheme.typography.labelLarge,
                                color = OceanDark,
                            )
                        }
                    }
                }
            }
        }
    }
}
