package com.m4ykey.albly.album.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumsDto(
    val items: List<AlbumItemDto>? = emptyList()
)