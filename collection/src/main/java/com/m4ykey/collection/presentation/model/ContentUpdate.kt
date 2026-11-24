package com.m4ykey.collection.presentation.model

import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.core.database.model.artist.ArtistListItem
import com.m4ykey.core.database.model.lyrics.LyricsEntity

data class ContentUpdate(
    val isLoading : Boolean? = null,
    val albums : List<AlbumListItem>? = null,
    val artists : List<ArtistListItem>? = null,
    val lyrics : List<LyricsEntity>? = null
)
