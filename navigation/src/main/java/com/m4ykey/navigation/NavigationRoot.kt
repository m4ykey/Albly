package com.m4ykey.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.m4ykey.album.presentation.detail.AlbumDetailScreen
import com.m4ykey.album.presentation.listen_later.ListenLaterScreen
import com.m4ykey.album.presentation.new_release.AlbumNewReleaseScreen
import com.m4ykey.collection.presentation.CollectionScreen
import com.m4ykey.search.presentation.SearchScreen
import com.m4ykey.settings.SettingsScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun NavigationRoot(
    modifier : Modifier = Modifier
) {
    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Collection::class, Route.Collection.serializer())
                    subclass(Route.NewRelease::class, Route.NewRelease.serializer())
                    subclass(Route.Search::class, Route.Search.serializer())
                    subclass(Route.Settings::class, Route.Settings.serializer())
                    subclass(Route.AlbumDetail::class, Route.AlbumDetail.serializer())
                    subclass(Route.ListenLater::class, Route.ListenLater.serializer())
                }
            }
        },
        Route.Collection
    )

    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = rootBackStack,
        entryProvider = entryProvider {
            entry<Route.Collection> {
                CollectionScreen(
                    navigateToSettings = {
                        rootBackStack.add(Route.Settings)
                    },
                    navigateToNewRelease = {
                        rootBackStack.add(Route.NewRelease)
                    },
                    navigateToListenLater = {
                        rootBackStack.add(Route.ListenLater)
                    },
                    onAlbumClick = {
                        rootBackStack.add(Route.AlbumDetail(it))
                    },
                    onSearch = {
                        rootBackStack.add(Route.Search)
                    },
                    onLinkClick = {
                        rootBackStack.add(Route.AlbumDetail(it))
                    }
                )
            }
            entry<Route.Search> {
                SearchScreen(
                    onAlbumClick = {
                        rootBackStack.add(Route.AlbumDetail(it))
                    },
                    onBack = {
                        if (rootBackStack.isNotEmpty()) {
                            rootBackStack.removeAt(rootBackStack.lastIndex)
                        }
                    }
                )
            }
            entry<Route.Settings> {
                SettingsScreen(
                    onBack = {
                        if (rootBackStack.isNotEmpty()) {
                            rootBackStack.removeAt(rootBackStack.lastIndex)
                        }
                    }
                )
            }
            entry<Route.ListenLater> {
                ListenLaterScreen(
                    onBack = {
                        if (rootBackStack.isNotEmpty()) {
                            rootBackStack.removeAt(rootBackStack.lastIndex)
                        }
                    },
                    onAlbumClick = {
                        rootBackStack.add(Route.AlbumDetail(it))
                    },
                    onSearchClick = {
                        rootBackStack.add(Route.Search)
                    }
                )
            }
            entry<Route.AlbumDetail> { key ->
                AlbumDetailScreen(
                    onBack = {
                        if (rootBackStack.isNotEmpty()) {
                            rootBackStack.removeAt(rootBackStack.lastIndex)
                        }
                    },
                    onTrackClick = { _, _ ->

                    },
                    id = key.albumId
                )
            }
            entry<Route.NewRelease> {
                AlbumNewReleaseScreen(
                    onBack = {
                        if (rootBackStack.isNotEmpty()) {
                            rootBackStack.removeAt(rootBackStack.lastIndex)
                        }
                    },
                    onAlbumClick = {
                        rootBackStack.add(Route.AlbumDetail(it))
                    }
                )
            }
        }
    )
}