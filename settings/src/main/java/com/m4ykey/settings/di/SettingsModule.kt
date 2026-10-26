package com.m4ykey.settings.di

import com.m4ykey.settings.SettingsViewModel
import com.m4ykey.settings.theme.ThemePreferences
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)

    single { ThemePreferences(dataStore = get()) }
}