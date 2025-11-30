package com.m4ykey.albly.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.m4ykey.albly.album.presentation.detail.AlbumDetailScreen
import com.m4ykey.albly.album.presentation.new_release.AlbumNewReleaseScreen
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
            route = Screen.CollectionScreen.screen
        ) {
            CollectionScreen(
                onSearch = {
                    navHostController.navigate(Screen.SearchScreen.screen)
                },
                onLinkClick = { albumId ->
                    navHostController.navigate(Screen.AlbumDetail.routeWithArgs(albumId = albumId))
                },
                onNavigateTo = { route ->
                    navHostController.navigate(route) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            route = Screen.SearchScreen.screen
        ) {
            SearchScreen(
                onBack = {
                    navHostController.navigateUp()
                },
                onAlbumClick = { albumId ->
                    navHostController.navigate(Screen.AlbumDetail.routeWithArgs(albumId = albumId))
                }
            )
        }

        composable(
            route = "${Screen.AlbumDetail.routeBase}/{${Screen.AlbumDetail.ARG_ALBUM_ID}}",
            arguments = listOf(
                navArgument(Screen.AlbumDetail.ARG_ALBUM_ID) { type = NavType.StringType }
            )
        ) { backStack ->
            val albumId = backStack.arguments?.getString(Screen.AlbumDetail.ARG_ALBUM_ID)
            AlbumDetailScreen(
                onBack = { navHostController.navigateUp() },
                id = albumId.orEmpty()
            )
        }

        composable(
            route = Screen.NewReleaseScreen.screen
        ) {
            AlbumNewReleaseScreen(
                onBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}