package com.m4ykey.albly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.albly.app.ui.theme.AlblyTheme
import com.m4ykey.navigation.NavigationRoot
import com.m4ykey.settings.theme.ThemePreferences
import com.m4ykey.settings.theme.ThemeType
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themePreferences : ThemePreferences = koinInject()

            val selectedTheme by themePreferences
                .getSelectedThemeOptions()
                .collectAsStateWithLifecycle(initialValue = ThemeType.DEFAULT)

            val darkTheme = when (selectedTheme) {
                ThemeType.DEFAULT -> isSystemInDarkTheme()
                ThemeType.DARK -> true
                ThemeType.LIGHT -> false
            }

            AlblyTheme(darkTheme = darkTheme) {
                NavigationRoot()
            }
        }
    }
}