package com.m4ykey.artist.presentation

sealed interface ArtistAction {
    data class OnLoadArtist(val id : Int) : ArtistAction
    data class OnLoadArtists(val ids : List<Int>) : ArtistAction
}