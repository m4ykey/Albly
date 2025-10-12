package com.m4ykey.albly.collection.presentation.type.album

sealed interface AlbumTypeAction {
    data class OnTypeClick(val type : AlbumType?) : AlbumTypeAction
}