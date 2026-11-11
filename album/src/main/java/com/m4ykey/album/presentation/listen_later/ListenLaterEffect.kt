package com.m4ykey.album.presentation.listen_later

sealed interface ListenLaterEffect {
    data class OpenAlbum(val id : Int) : ListenLaterEffect
    data class ShowError(val message : String) : ListenLaterEffect
    data object AlbumListEmpty : ListenLaterEffect
    data object NotEnoughAlbums : ListenLaterEffect
}