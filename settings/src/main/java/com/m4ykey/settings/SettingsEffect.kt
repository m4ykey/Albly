package com.m4ykey.settings

sealed interface SettingsEffect {
    data class OpenLink(val url : String) : SettingsEffect
    data class ShowError(val message : String) : SettingsEffect
    data object ClearImageCache : SettingsEffect
}