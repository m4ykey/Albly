package com.m4ykey.albly.album.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlbumsDto(
    val items: List<AlbumItemDto>? = emptyList()
)