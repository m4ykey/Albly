package com.m4ykey.album.presentation.detail

sealed interface AlbumDetailEffect {
    data class ShowError(val message : String) : AlbumDetailEffect
}