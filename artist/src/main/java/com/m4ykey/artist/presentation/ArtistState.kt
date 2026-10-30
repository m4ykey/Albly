package com.m4ykey.artist.presentation

import com.m4ykey.artist.domain.model.Artist

sealed interface ArtistState {
    data object Loading : ArtistState
    data class Error(val message : String) : ArtistState
    data class Success(val artists : List<Artist>) : ArtistState
}