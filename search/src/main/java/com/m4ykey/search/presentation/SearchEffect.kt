package com.m4ykey.search.presentation

sealed interface SearchEffect {
    data class OnAlbumClick(val id : Int) : SearchEffect
    data class OnArtistClick(val id : Int) : SearchEffect
    data class OnTrackClick(
        val title : String,
        val artist : String,
        val img : String,
        val album : String?
    ) : SearchEffect
}