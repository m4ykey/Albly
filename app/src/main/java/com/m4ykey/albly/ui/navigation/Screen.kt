package com.m4ykey.albly.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val screen : String) {

    @Serializable
    object CollectionScreen : Screen(screen = "collection_screen")

    @Serializable
    object NewsScreen : Screen(screen = "news_screen")

    @Serializable
    object SearchScreen : Screen(screen = "search_screen")

}