package com.m4ykey.settings

import com.m4ykey.settings.theme.ThemeType

data class SettingsUiState(
    val selectedTheme : ThemeType = ThemeType.DEFAULT,
    val isThemeDialogVisible : Boolean = false
)
