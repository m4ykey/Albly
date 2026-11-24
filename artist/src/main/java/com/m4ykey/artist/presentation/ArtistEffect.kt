package com.m4ykey.artist.presentation

sealed interface ArtistEffect {
    data class ShowError(val message : String) : ArtistEffect
}