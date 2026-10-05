package com.m4ykey.collection.presentation

import com.m4ykey.album.data.local.model.AlbumListItem
import com.m4ykey.artist.local.ArtistListItem

data class CollectionUiState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val albums : List<AlbumListItem> = emptyList(),
    val artists : List<ArtistListItem> = emptyList()
)
