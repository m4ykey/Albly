package com.m4ykey.artist.presentation

import com.m4ykey.artist.domain.model.Artist

sealed interface ArtistDialogState {
    data object Loading : ArtistDialogState
    data class Error(val message : String) : ArtistDialogState
    data class Success(val artists : List<Artist>) : ArtistDialogState
}