package com.m4ykey.collection.presentation

import com.m4ykey.artist.data.local.ArtistListItem
import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.core.database.model.lyrics.LyricsEntity

data class ContentUpdate(
    val isLoading : Boolean? = null,
    val albums : List<AlbumListItem>? = null,
    val artists : List<ArtistListItem>? = null,
    val lyrics : List<LyricsEntity>? = null
)
