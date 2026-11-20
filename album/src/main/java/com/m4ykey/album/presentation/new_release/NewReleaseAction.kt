package com.m4ykey.album.presentation.new_release

sealed interface NewReleaseAction {
    data class OnAlbumClick(val id : Int) : NewReleaseAction
    data object OnHideAlbumDialog : NewReleaseAction
    data class OnLongAlbumClick(val id : Int) : NewReleaseAction
}