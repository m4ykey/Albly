package com.m4ykey.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Search : Route

    @Serializable
    data object Collection : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object ListenLater : Route

    @Serializable
    data class AlbumDetail(val albumId : String) : Route

    @Serializable
    data object NewRelease : Route

}