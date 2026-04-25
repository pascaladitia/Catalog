package com.pascal.catalog.feature.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.feature.auth.presentation.login.state.LocalLoginEvent
import com.pascal.catalog.feature.auth.presentation.login.state.LocalLoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(

): ViewModel() {

    private val _uiState = MutableStateFlow(LocalLoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LocalLoginEvent) {
        when (event) {
            is LocalLoginEvent.OnEmailChange -> {
                _uiState.update { it.copy(email = false to event.value) }
            }

            is LocalLoginEvent.OnPasswordChange -> {
                _uiState.update { it.copy(password = false to event.value) }
            }

            is LocalLoginEvent.OnPasswordVisibility -> {
                _uiState.update { it.copy(passwordVisibility = !it.passwordVisibility) }
            }

            is LocalLoginEvent.OnSubmit -> {
                if (_uiState.value.email.second.isBlank()) {
                    _uiState.update { it.copy(email = true to _uiState.value.email.second) }
                    return
                }

                if (_uiState.value.password.second.isBlank()) {
                    _uiState.update { it.copy(email = true to _uiState.value.password.second) }
                    return
                }

                _uiState.update { it.copy(isLoginSuccess = true) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { LocalLoginUiState() }
    }
}