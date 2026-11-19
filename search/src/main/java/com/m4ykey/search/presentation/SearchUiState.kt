package com.m4ykey.search.presentation

data class SearchUiState(
    val query : String = "",
    val type : SearchType = SearchType.ALBUM,
    val activeQuery : String = "",
    val selectedAlbumId : Int? = null,
    val removeOneElement : Boolean = false
)
