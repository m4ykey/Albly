package com.m4ykey.albly.album.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumItemDto(
    val album_type: String? = "",
    val artists: List<AlbumArtistDto>? = emptyList(),
    val external_urls: ExternalUrlsDto? = null,
    val id: String? = "",
    val images: List<ImageDto>? = emptyList(),
    val name: String? = "",
    val release_date: String? = "",
    val total_tracks: Int? = 0,
    val type: String? = ""
)