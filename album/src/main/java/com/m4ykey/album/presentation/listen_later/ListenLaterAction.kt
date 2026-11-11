package com.m4ykey.album.presentation.listen_later

sealed interface ListenLaterAction {
    data object OnLoadAlbums : ListenLaterAction
    data object OnRandomAlbumClick : ListenLaterAction
}