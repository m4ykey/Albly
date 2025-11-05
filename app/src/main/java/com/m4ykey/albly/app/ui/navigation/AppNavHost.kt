package com.m4ykey.albly.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.m4ykey.albly.collection.presentation.CollectionScreen
import com.m4ykey.albly.search.presentation.SearchScreen

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
        composable(
            route = Screen.CollectionScreen.screen,
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            CollectionScreen(
                onSearch = {
                    navHostController.navigate(Screen.SearchScreen.screen)
                }
            )
        }

        composable(
            route = Screen.SearchScreen.screen,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            SearchScreen(
                onBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}