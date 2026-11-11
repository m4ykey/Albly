package com.m4ykey.albly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.albly.app.ui.MainActivityViewModel
import com.m4ykey.albly.app.ui.theme.AlblyTheme
import com.m4ykey.navigation.NavigationRoot
import com.m4ykey.settings.theme.ThemeType
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel : MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { !viewModel.uiState.value.isThemeLoaded }

        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            uiState.selectedTheme?.let { theme ->
                val darkTheme = when (theme) {
                    ThemeType.DEFAULT -> isSystemInDarkTheme()
                    ThemeType.DARK -> true
                    ThemeType.LIGHT -> false
                }

                SideEffect {
                    val controller = WindowCompat.getInsetsController(
                        window,
                        window.decorView
                    )

                    controller.isAppearanceLightStatusBars = !darkTheme
                    controller.isAppearanceLightNavigationBars = !darkTheme
                }

                AlblyTheme(darkTheme = darkTheme) {
                    NavigationRoot()
                }
            }
        }
    }
}
