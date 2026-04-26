package com.pascal.catalog.feature.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pascal.catalog.core.designsystem.R
import com.pascal.catalog.core.designsystem.component.ButtonComponent
import com.pascal.catalog.core.designsystem.component.FormEmailComponent
import com.pascal.catalog.core.designsystem.component.FormPasswordComponent
import com.pascal.catalog.core.designsystem.theme.CatalogTheme
import com.pascal.catalog.feature.auth.navigation.AuthDestination
import com.pascal.catalog.feature.auth.presentation.login.state.LocalLoginEvent
import com.pascal.catalog.feature.auth.presentation.login.state.LocalLoginUiState
import com.pascal.catalog.feature.catalog.navigation.CatalogDestination

@Composable
fun LoginRoute(
    onRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) onLoginSuccess()
    }

    LoginScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onRegister = onRegister
    )
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LocalLoginUiState = LocalLoginUiState(),
    onEvent: (LocalLoginEvent) -> Unit = {},
    onRegister: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.label_app_name),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.label_title_login),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(Modifier.height(48.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            FormEmailComponent(
                title = stringResource(R.string.label_email_username),
                hintText = stringResource(R.string.hint_email_username),
                value = uiState.email.second,
                onValueChange = { onEvent(LocalLoginEvent.OnEmailChange(it)) },
                isError = uiState.email.first
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormPasswordComponent(
                title = stringResource(R.string.label_password),
                hintText = stringResource(R.string.hint_password),
                value = uiState.password.second,
                onValueChange = { onEvent(LocalLoginEvent.OnPasswordChange(it)) },
                isError = uiState.password.first,
                isPasswordVisible = uiState.passwordVisibility,
                onIconClick = {
                    onEvent(LocalLoginEvent.OnPasswordVisibility)
                }
            ) {
                keyboardController?.hide()
                onEvent(LocalLoginEvent.OnSubmit)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ButtonComponent(text = stringResource(R.string.label_login)) {
                keyboardController?.hide()
                onEvent(LocalLoginEvent.OnSubmit)
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.message_dont_have_account),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.clickable { onRegister() },
                text = stringResource(R.string.label_register),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginPreview() {
    CatalogTheme {
        LoginScreen()
    }
}
