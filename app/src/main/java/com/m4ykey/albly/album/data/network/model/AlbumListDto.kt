package com.m4ykey.albly.album.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumListDto(
    val albums: AlbumsDto
)