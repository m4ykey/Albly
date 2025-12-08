package com.m4ykey.albly.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val screen : String) {

    @Serializable
    object CollectionScreen : Screen(screen = "collection_screen")

    @Serializable
    object SearchScreen : Screen(screen = "search_screen")

    @Serializable
    data class AlbumDetail(val albumId : String) : Screen("$routeBase/$albumId") {
        companion object {
            const val routeBase = "album_detail_screen"
            const val ARG_ALBUM_ID = "albumId"

            fun routeWithArgs(albumId: String) : String =
                "$routeBase/$albumId"
        }
    }

    @Serializable
    object NewReleaseScreen : Screen(screen = "new_release_screen")

    @Serializable
    object ListenLaterScreen : Screen(screen = "listen_later_screen")

    @Serializable
    data class LyricsScreen(val trackName : String, val artistName : String) : Screen(
        "$routeBase/$trackName&$artistName"
    ) {
        companion object {
            const val routeBase = "lyrics_screen"
            const val ARG_ARTIST_NAME = "artist_name"
            const val ARG_TRACK_NAME = "track_name"

            fun routeWithArgs(artistName: String, trackName: String) =
                "$routeBase/$artistName&$trackName"
        }
    }

}