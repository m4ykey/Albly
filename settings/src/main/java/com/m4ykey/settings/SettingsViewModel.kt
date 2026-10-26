package com.m4ykey.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.ThemeType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val selectedTheme : StateFlow<ThemeType> =
        themePreferences.getSelectedThemeOptions()
            .stateIn(
                scope = viewModelScope,
                initialValue = ThemeType.DEFAULT,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    fun changeTheme(theme : ThemeType) {
        viewModelScope.launch {
            themePreferences.saveThemeOptions(theme)
        }
    }

}