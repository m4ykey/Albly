package com.m4ykey.albly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.m4ykey.albly.collection.presentation.CollectionScreen
import com.m4ykey.albly.search.SearchScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        modifier = modifier,
        startDestination = Screen.CollectionScreen.screen
    ) {
        composable(Screen.CollectionScreen.screen) {
            CollectionScreen(
                onSearch = {
                    navHostController.navigate(Screen.SearchScreen.screen)
                }
            )
        }

        composable(Screen.SearchScreen.screen) {
            SearchScreen(
                onNavBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}