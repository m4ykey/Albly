package com.m4ykey.album.presentation.new_release

sealed interface NewReleaseEffect {
    data class OnAlbumClick(val id : Int) : NewReleaseEffect
}