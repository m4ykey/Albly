package com.m4ykey.albly.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(themePreferences: ThemePreferences) : ViewModel() {

    val selectedTheme : StateFlow<ThemeType?> =
        themePreferences.getSelectedThemeOptions()
            .map<ThemeType, ThemeType?> { it }
            .stateIn(
                scope = viewModelScope,
                initialValue = null,
                started = SharingStarted.Eagerly
            )

}