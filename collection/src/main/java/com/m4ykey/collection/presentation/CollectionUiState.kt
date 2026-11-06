package com.m4ykey.collection.presentation

import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.artist.data.local.ArtistListItem
import com.m4ykey.core.database.model.lyrics.LyricsEntity

data class CollectionUiState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val albums : List<AlbumListItem> = emptyList(),
    val artists : List<ArtistListItem> = emptyList(),
    val lyrics : List<LyricsEntity> = emptyList()
)
