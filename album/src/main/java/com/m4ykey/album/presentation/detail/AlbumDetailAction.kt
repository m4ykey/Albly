package com.m4ykey.album.presentation.detail

sealed interface AlbumDetailAction {
    data class OnLoadAlbum(val id : Int) : AlbumDetailAction
    data object OnToggleSave : AlbumDetailAction
    data object OnToggleListenLater : AlbumDetailAction
}