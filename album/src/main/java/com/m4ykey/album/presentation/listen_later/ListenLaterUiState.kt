package com.m4ykey.album.presentation.listen_later

import com.m4ykey.core.database.model.album.AlbumEntity
import com.m4ykey.core.database.model.album.AlbumListItem

data class ListenLaterUiState(
    val albums : List<AlbumListItem> = emptyList(),
    val randomAlbum : AlbumEntity? = null,
    val error : String? = null,
    val isLoading : Boolean = false
)
