package com.pascal.catalog.feature.auth.presentation

import com.google.common.truth.Truth
import com.pascal.catalog.core.domain.usecase.LoginUseCase
import com.pascal.catalog.feature.auth.presentation.common.FakeAuthRepository
import com.pascal.catalog.feature.auth.presentation.common.MainDispatcherRule
import com.pascal.catalog.feature.auth.presentation.login.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `login success updates state`() = runTest {
        val repository = FakeAuthRepository()

        val viewModel = LoginViewModel(
            loginUseCase = LoginUseCase(repository),
        )

        val job = backgroundScope.launch {
            viewModel.uiState.collect {}
        }

        viewModel.onEmailChange("user")
        viewModel.onPasswordChange("user")
        viewModel.onSubmit()

        advanceUntilIdle()

        Truth.assertThat(viewModel.uiState.value.isLoginSuccess).isTrue()

        job.cancel()
    }
}
