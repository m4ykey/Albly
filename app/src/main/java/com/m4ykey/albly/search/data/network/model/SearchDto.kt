package com.m4ykey.albly.search.data.network.model

import com.m4ykey.albly.album.data.network.model.AlbumsDto
import com.m4ykey.albly.artist.data.network.model.ArtistsDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchDto(
    val albums: AlbumsDto,
    val artists: ArtistsDto
)