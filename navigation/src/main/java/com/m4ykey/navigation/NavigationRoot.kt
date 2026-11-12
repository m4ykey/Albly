@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.m4ykey.album.presentation.cover.AlbumCoverScreen
import com.m4ykey.album.presentation.detail.AlbumDetailScreen
import com.m4ykey.album.presentation.listen_later.ListenLaterScreen
import com.m4ykey.album.presentation.new_release.AlbumNewReleaseScreen
import com.m4ykey.album.presentation.video.AlbumVideoScreen
import com.m4ykey.artist.presentation.ArtistScreen
import com.m4ykey.collection.presentation.CollectionScreen
import com.m4ykey.lyrics.presentation.LyricsScreen
import com.m4ykey.search.presentation.SearchScreen
import com.m4ykey.settings.SettingsScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal val navConfiguration = SavedStateConfiguration {
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
            subclass(Route.Artist::class, Route.Artist.serializer())
            subclass(Route.Video::class, Route.Video.serializer())
        }
    }
}

@Composable
fun NavigationRoot(
    modifier : Modifier = Modifier,
    backStack : NavBackStack<NavKey> = rememberNavBackStack(navConfiguration, Route.Collection),
    entryProviderFactory : (AppNavigator) -> (NavKey) -> NavEntry<NavKey> = ::appEntryProvider
) {
    val navigator = remember(backStack) { AppNavigator(backStack) }

    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            slideInHorizontally { it } togetherWith
                    slideOutHorizontally { -it }
        },
        popTransitionSpec = {
            slideInHorizontally { -it } togetherWith
                    slideOutHorizontally { it }
        },
        predictivePopTransitionSpec = {
            slideInHorizontally { -it } togetherWith
                    slideOutHorizontally { it }
        },
        onBack = navigator::navigateBack,
        backStack = backStack,
        entryProvider = entryProviderFactory(navigator)
    )
}

internal fun appEntryProvider(
    navigator: AppNavigator
) : (NavKey) -> NavEntry<NavKey> = entryProvider {
    entry<Route.Collection> {
        CollectionScreen(
            navigateToSettings = { navigator.navigateTo(Route.Settings) },
            navigateToNewRelease = { navigator.navigateTo(Route.NewRelease) },
            navigateToListenLater = { navigator.navigateTo(Route.ListenLater) },
            onAlbumClick = { navigator.navigateTo(Route.AlbumDetail(it)) },
            onSearch = { navigator.navigateTo(Route.Search) },
            onLinkClick = {  },
            onLyricsClick = { track, artist, image, album ->
                navigator.navigateTo(Route.Lyrics(
                    track = track,
                    artist = artist,
                    img = image,
                    album = album))
            }
        )
    }
    entry<Route.Search> {
        SearchScreen(
            onAlbumClick = { albumId ->
                navigator.navigateTo(Route.AlbumDetail(albumId = albumId))
            },
            onBack = navigator::navigateBack,
            onTrackClick = { track, artist, img, album ->
                navigator.navigateTo(Route.Lyrics(track, artist, img, album))
            },
            onArtistClick = { artistId ->
                navigator.navigateTo(Route.Artist(artistId))
            }
        )
    }
    entry<Route.Settings> {
        SettingsScreen(onBack = navigator::navigateBack)
    }
    entry<Route.ListenLater> {
        ListenLaterScreen(
            onBack = navigator::navigateBack,
            onAlbumClick = {
                navigator.navigateTo(Route.AlbumDetail(it))
            },
            onSearchClick = {
                navigator.navigateTo(Route.Search)
            }
        )
    }
    entry<Route.AlbumDetail> { key ->
        AlbumDetailScreen(
            onBack = navigator::navigateBack,
            onTrackClick = { track ->
                navigator.navigateTo(Route.Lyrics(
                    track = track.title,
                    artist = track.artists,
                    img = track.img,
                    album = track.album
                ))
            },
            id = key.albumId,
            onCoverClick = { image ->
                navigator.navigateTo(Route.Cover(image))
            },
            onArtistClick = { artistId ->
                navigator.navigateTo(Route.Artist(artistId))
            },
            onVideoClick = { videos ->
                navigator.navigateTo(Route.Video(videos))
            }
        )
    }
    entry<Route.NewRelease> {
        AlbumNewReleaseScreen(
            onBack = navigator::navigateBack,
            onAlbumClick = {
                navigator.navigateTo(Route.AlbumDetail(it))
            }
        )
    }
    entry<Route.Lyrics> { key ->
        LyricsScreen(
            onBack = navigator::navigateBack,
            artistName = key.artist,
            trackName = key.track,
            imageUrl = key.img.orEmpty(),
            albumName = key.album,
            onImageClick = { item ->
                navigator.navigateTo(Route.Cover(item))
            }
        )
    }
    entry<Route.Cover> { key ->
        AlbumCoverScreen(
            imageUrl = key.imageUrl,
            onBack = navigator::navigateBack
        )
    }
    entry<Route.Artist> { key ->
        ArtistScreen(
            artistId = key.artistId,
            onBack = navigator::navigateBack,
            onAlbumClick = { id ->
                navigator.navigateTo(Route.AlbumDetail(albumId = id))
            }
        )
    }
    entry<Route.Video> { key ->
        AlbumVideoScreen(
            onBack = navigator::navigateBack,
            videos = key.videos
        )
    }
}