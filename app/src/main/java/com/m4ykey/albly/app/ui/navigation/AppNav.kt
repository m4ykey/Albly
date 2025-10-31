package com.m4ykey.albly.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNav() {
    val navHostController = rememberNavController()
    AppNavHost(navHostController = navHostController)
}