package com.m4ykey.search.presentation

sealed interface SearchAction {
    data class OnTypeClick(val type : SearchType) : SearchAction
    data class OnQueryChange(val query : String) : SearchAction
    data object OnSearchClick : SearchAction
    data class OnAlbumClick(val id : Int) : SearchAction
    data class OnTrackClick(val title : String, val artist : String, val img : String, val album : String?) : SearchAction
    data class OnArtistClick(val id : Int) : SearchAction
    data class OnLongAlbumClick(val id : Int) : SearchAction
    data object OnHideAlbumDialog : SearchAction
}