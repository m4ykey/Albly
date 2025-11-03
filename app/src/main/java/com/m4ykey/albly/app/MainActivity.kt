package com.m4ykey.albly.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.m4ykey.albly.BuildConfig
import com.m4ykey.albly.app.ui.navigation.AppNav
import com.m4ykey.albly.app.ui.theme.AlblyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlblyTheme {
                AppNav()

                Log.i("SpotifyKey", "getAccessToken: ${BuildConfig.SPOTIFY_CLIENT_SECRET}")
                Log.i("SpotifyKey", "getAccessToken: ${BuildConfig.SPOTIFY_CLIENT_ID}")
            }
        }
    }
}