package com.m4ykey.albly.artist.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistsDto(
    val items: List<ArtistItemDto>? = emptyList()
)