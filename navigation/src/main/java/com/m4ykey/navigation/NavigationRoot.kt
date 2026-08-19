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
import com.m4ykey.album.presentation.detail.AlbumCoverScreen
import com.m4ykey.album.presentation.detail.AlbumDetailScreen
import com.m4ykey.album.presentation.listen_later.ListenLaterScreen
import com.m4ykey.album.presentation.new_release.AlbumNewReleaseScreen
import com.m4ykey.collection.presentation.CollectionScreen
import com.m4ykey.lyrics.presentation.LyricsScreen
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
                    subclass(Route.Lyrics::class, Route.Lyrics.serializer())
                    subclass(Route.Cover::class, Route.Cover.serializer())
                }
            }
        },
        Route.Collection
    )

    fun navigateBack() {
        if (rootBackStack.size > 1) {
            rootBackStack.removeAt(rootBackStack.lastIndex)
        }
    }

    fun navigateTo(route: Route) {
        rootBackStack.add(route)
    }

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
                    navigateToSettings = { navigateTo(Route.Settings) },
                    navigateToNewRelease = { navigateTo(Route.NewRelease) },
                    navigateToListenLater = { navigateTo(Route.ListenLater) },
                    onAlbumClick = { navigateTo(Route.AlbumDetail(it)) },
                    onSearch = { navigateTo(Route.Search) },
                    onLinkClick = {  }
                )
            }
            entry<Route.Search> {
                SearchScreen(
                    onAlbumClick = { albumId ->
                        navigateTo(Route.AlbumDetail(albumId = albumId))
                    },
                    onBack = ::navigateBack,
                    onTrackClick = { title, artist, img ->
                        navigateTo(Route.Lyrics(title, artist, img))
                    }
                )
            }
            entry<Route.Settings> {
                SettingsScreen(
                    onBack = ::navigateBack
                )
            }
            entry<Route.ListenLater> {
                ListenLaterScreen(
                    onBack = ::navigateBack,
                    onAlbumClick = {
                        navigateTo(Route.AlbumDetail(it))
                    },
                    onSearchClick = {
                        navigateTo(Route.Search)
                    }
                )
            }
            entry<Route.AlbumDetail> { key ->
                AlbumDetailScreen(
                    onBack = ::navigateBack,
                    onTrackClick = { artist, title, img ->
                        navigateTo(Route.Lyrics(title = title, artist = artist, img = img))
                    },
                    id = key.albumId,
                    onCoverClick = { image ->
                        navigateTo(Route.Cover(image))
                    }
                )
            }
            entry<Route.NewRelease> {
                AlbumNewReleaseScreen(
                    onBack = ::navigateBack,
                    onAlbumClick = {
                        navigateTo(Route.AlbumDetail(it))
                    }
                )
            }
            entry<Route.Lyrics> { key ->
                LyricsScreen(
                    onBack = ::navigateBack,
                    artistName = key.artist,
                    trackName = key.title,
                    imageUrl = key.img
                )
            }
            entry<Route.Cover> { key ->
                AlbumCoverScreen(
                    imageUrl = key.imageUrl,
                    onBack = ::navigateBack
                )
            }
        }
    )
}