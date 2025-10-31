package com.m4ykey.albly.album.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumListDto(
    val albums: AlbumsDto
)