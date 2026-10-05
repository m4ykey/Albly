package com.m4ykey.album.presentation.listen_later

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.AlbumListItem

data class ListenLaterUiState(
    val albums : List<AlbumListItem> = emptyList(),
    val randomAlbum : AlbumEntity? = null,
    val error : String? = null,
    val isLoading : Boolean = false
)
