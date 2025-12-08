package com.m4ykey.albly.collection.presentation

import com.m4ykey.albly.collection.presentation.type.album.AlbumType

sealed interface CollectionTypeAction {
    data class OnTypeClick(val type : AlbumType?) : CollectionTypeAction
    data class OnLinkClick(val link : String) : CollectionTypeAction
    data class OnQueryChange(val text : String) : CollectionTypeAction
}