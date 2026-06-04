package com.m4ykey.search.presentation

sealed interface SearchUiEvent {
    data class ChangeType(val type : SearchType) : SearchUiEvent
    data class OnAlbumClick(val id : Int) : SearchUiEvent
    data class OnTrackClick(val title : String, val artist : String, val img : String) : SearchUiEvent
}