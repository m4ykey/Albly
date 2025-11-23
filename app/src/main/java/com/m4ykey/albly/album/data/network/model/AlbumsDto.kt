package com.m4ykey.albly.album.data.network.model

data class AlbumsDto(
    val items: List<AlbumItemDto>? = emptyList()
)