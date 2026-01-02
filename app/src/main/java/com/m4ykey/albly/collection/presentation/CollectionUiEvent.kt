package com.m4ykey.albly.collection.presentation

import com.m4ykey.albly.collection.presentation.type.album.AlbumType

sealed interface CollectionUiEvent {
    data class OnLinkClick(val link : String) : CollectionUiEvent
}