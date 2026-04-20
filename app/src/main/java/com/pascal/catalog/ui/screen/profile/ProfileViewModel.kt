package com.pascal.catalog.ui.screen.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.catalog.data.prefs.PrefsTheme
import com.pascal.catalog.ui.screen.profile.state.ProfileUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val app: Application
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            PrefsTheme.observeThemeMode(app).collect { mode ->
                _uiState.value = _uiState.value.copy(
                    themeMode = runCatching { ThemeMode.valueOf(mode) }.getOrDefault(ThemeMode.SYSTEM)
                )
            }
        }
    }

    fun onThemeChanged(mode: ThemeMode) {
        _uiState.value = _uiState.value.copy(themeMode = mode)
        viewModelScope.launch { PrefsTheme.saveThemeMode(app, mode.name) }
    }

    fun resetError() {
        _uiState.update { it.copy(error = false to "") }
    }
}

enum class ThemeMode { SYSTEM, LIGHT, DARK }