package com.m4ykey.collection.presentation

import com.m4ykey.core.model.type.AlbumType

sealed interface CollectionTypeAction {
    data class OnTypeClick(val type : AlbumType?) : CollectionTypeAction
    data class OnLinkClick(val link : String) : CollectionTypeAction
    data class OnQueryChange(val text : String) : CollectionTypeAction
}