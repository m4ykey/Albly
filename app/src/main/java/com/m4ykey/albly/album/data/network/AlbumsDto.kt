package com.m4ykey.albly.album.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumsDto(
    val items: List<AlbumItemDto>? = emptyList()
)