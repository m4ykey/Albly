package com.m4ykey.search.presentation

sealed interface SearchUiEvent {
    data class ChangeType(val type : SearchType) : SearchUiEvent
    data class OnAlbumClick(val id : String) : SearchUiEvent
}