package com.m4ykey.artist.presentation

data class ArtistUiState(
    val artistId : Int? = null,
    val artistState: ArtistState? = null,
    val isRefreshing : Boolean = false,
    val isSaved : Boolean = false,
    val isSaving : Boolean = false
)
