package com.pascal.catalog.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.core.domain.usecase.LoginUseCase
import com.pascal.catalog.feature.auth.presentation.login.state.LocalLoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocalLoginUiState())
    val uiState: StateFlow<LocalLoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value to false) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value to false) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onSubmit() {
        val state = _uiState.value
        val isEmailBlank = state.email.first.isBlank()
        val isPasswordBlank = state.password.first.isBlank()

        if (isEmailBlank || isPasswordBlank) {
            val message = when {
                isEmailBlank -> "Email tidak boleh kosong"
                isPasswordBlank -> "Password tidak boleh kosong"
                else -> ""
            }
            _uiState.update { it.copy(error = true to message) }
            return
        }

        loadLogin()
    }

    private fun loadLogin() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            loginUseCase(_uiState.value.email.first, _uiState.value.password.first)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = true to e.message.orEmpty(),
                        )
                    }
                }
                .collect {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccess = true,
                        )
                    }
                }
        }
    }

    fun hideDialog() {
        _uiState.update { it.copy(error = false to "") }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.value = LocalLoginUiState()
    }
}
