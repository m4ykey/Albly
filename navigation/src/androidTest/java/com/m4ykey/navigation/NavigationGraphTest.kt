package com.m4ykey.navigation

import androidx.navigation3.runtime.NavKey
import com.m4ykey.album.domain.model.detail.Video
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.Assert.assertEquals

class NavigationGraphTest {

    private val navigator = AppNavigator(mutableListOf(Route.Collection))
    private val provider = appEntryProvider(navigator)

    private val routes : List<Route> = listOf(
        Route.Collection,
        Route.Search,
        Route.Settings,
        Route.ListenLater,
        Route.NewRelease,
        Route.AlbumDetail(1),
        Route.Cover("https://example.com/cover.jpg"),
        Route.Lyrics(track = "Track", artist = "Artist", img = null, album = null),
        Route.Artist(1),
        Route.Video(listOf(Video("https://youtu.be/teafadsf234", "Title", "description", 10, true)))
    )

    private val json = Json {
        serializersModule = navConfiguration.serializersModule
    }

    @Test
    fun everyRouteHasEntryInProvider() {
        routes.forEach { route ->
            provider(route)
        }
    }

    @Test
    fun everyRouteSurvivesPolymorphicSerialization() {
        routes.forEach { route ->
            val encoded = json.encodeToString(PolymorphicSerializer(NavKey::class), route)
            val decoded = json.decodeFromString(PolymorphicSerializer(NavKey::class), encoded)
            assertEquals(route, decoded)
        }
    }
}