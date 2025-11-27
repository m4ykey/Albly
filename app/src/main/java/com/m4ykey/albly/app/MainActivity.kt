package com.m4ykey.albly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.m4ykey.albly.app.ui.navigation.AppNav
import com.m4ykey.albly.app.ui.theme.AlblyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlblyTheme {
                AppNav()
            }
        }
    }
}