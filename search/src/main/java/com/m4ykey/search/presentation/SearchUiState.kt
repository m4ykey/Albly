package com.m4ykey.search.presentation

data class SearchUiState(
    val query : String = "",
    val type : SearchType = SearchType.ALBUM,
    val activeQuery : String = ""
)
