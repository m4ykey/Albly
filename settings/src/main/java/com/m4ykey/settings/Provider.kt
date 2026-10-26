package com.m4ykey.settings

import androidx.annotation.DrawableRes

data class Provider(
    val name : String,
    val url : String,
    @DrawableRes val image : Int
)

val dataProviders = listOf(
    Provider("Discogs", "https://www.discogs.com/", R.drawable.discogs)
)

val lyricsProvider = listOf(
    Provider("LRCLIB", "https://lrclib.net/", R.drawable.lrclib),
    Provider("LRC Red", "https://lrc.red/", R.drawable.lrc_red)
)

val searchProvider = listOf(
    Provider("Genius", "https://genius.com/", R.drawable.genius)
)