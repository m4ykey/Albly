package com.m4ykey.albly.app.ui

import com.m4ykey.settings.theme.ThemeType

data class MainActivityUiState(
    val selectedTheme : ThemeType? = null
) {
    val isThemeLoaded : Boolean
        get() = selectedTheme != null
}
