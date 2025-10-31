package com.m4ykey.albly.album.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumArtistDto(
    val external_urls: ExternalUrlsDto? = null,
    val id: String? = "",
    val name: String? = "",
    val type: String? = ""
)