package com.m4ykey.albly

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.m4ykey.albly.ui.navigation.AppNavHost

@Composable
fun AppNav() {
    val navHostController = rememberNavController()
    AppNavHost(navHostController = navHostController)
}