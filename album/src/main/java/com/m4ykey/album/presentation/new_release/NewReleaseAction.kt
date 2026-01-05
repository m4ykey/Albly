package com.m4ykey.album.presentation.new_release

sealed interface NewReleaseAction {
    data class OnAlbumClick(val id : String) : NewReleaseAction
}