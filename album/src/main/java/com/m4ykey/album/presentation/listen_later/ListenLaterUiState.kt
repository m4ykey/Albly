package com.m4ykey.album.presentation.listen_later

import com.m4ykey.core.database.model.album.AlbumListItem

data class ListenLaterUiState(
    val albums : List<AlbumListItem> = emptyList(),
    val isLoading : Boolean = false
)
