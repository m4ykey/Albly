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

}