package com.m4ykey.albly.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.ThemeType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(themePreferences: ThemePreferences) : ViewModel() {

    val uiState : StateFlow<MainActivityUiState> =
        themePreferences.getSelectedThemeOptions()
            .map { theme -> MainActivityUiState(selectedTheme = theme) }
            .catch { exception ->
                if (exception is CancellationException) throw exception
                emit(MainActivityUiState(selectedTheme = ThemeType.DEFAULT))
            }
            .stateIn(
                scope = viewModelScope,
                initialValue = MainActivityUiState(),
                started = SharingStarted.Eagerly
            )

}