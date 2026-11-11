package com.m4ykey.settings

import com.m4ykey.settings.theme.ThemeType

sealed interface SettingsAction {

    data object OnShowThemeDialog : SettingsAction
    data object OnHideThemeDialog : SettingsAction
    data class OnChangeTheme(val theme : ThemeType) : SettingsAction

    data object OnOpenSource : SettingsAction
    data object OnClearImageCache: SettingsAction

}