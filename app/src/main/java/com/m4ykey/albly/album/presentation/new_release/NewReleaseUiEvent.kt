package com.m4ykey.albly.album.presentation.new_release

sealed interface NewReleaseUiEvent {
    data class OnAlbumClick(val id : String) : NewReleaseUiEvent
}