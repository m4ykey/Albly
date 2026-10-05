package com.m4ykey.collection.presentation

import com.m4ykey.album.data.local.model.AlbumListItem

data class SavedAlbumsUiState(
    val isLoading : Boolean = false,
    val error : String? = null,
    val albums : List<AlbumListItem> = emptyList()
)
