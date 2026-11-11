package com.m4ykey.collection.presentation

import com.m4ykey.artist.data.local.ArtistListItem
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListTypeState
import com.m4ykey.collection.presentation.type.ListViewType
import com.m4ykey.core.Sort
import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.core.database.model.lyrics.LyricsEntity

data class CollectionUiState(
    val isLoading : Boolean = false,
    val albums : List<AlbumListItem> = emptyList(),
    val artists : List<ArtistListItem> = emptyList(),
    val lyrics : List<LyricsEntity> = emptyList(),
    val listTypeState : ListTypeState = ListTypeState(
        view = ListViewType.GRID,
        list = ListType.ALBUM,
        sort = Sort.LATEST
    ),
    val searchQuery : String = "",
    val isSearchVisible : Boolean = false,
    val isSortDialogVisible : Boolean = false,
    val isLinkDialogVisible : Boolean = false
) {
    val isEmpty : Boolean
        get() = when (listTypeState.list) {
            ListType.ALBUM -> albums.isEmpty()
            ListType.ARTIST -> artists.isEmpty()
            ListType.LYRICS -> lyrics.isEmpty()
        }
}
