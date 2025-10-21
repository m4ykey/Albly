package com.m4ykey.albly.album.data.network.model

import com.m4ykey.albly.artist.data.network.model.ArtistDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumItemDto(
    @param:Json(name = "album_type") val albumType: String? = "",
    val artists: List<ArtistDto>? = emptyList(),
    @param:Json(name = "external_urls") val externalUrls: ExternalUrlsDto? = null,
    val id: String? = "",
    val images: List<ImageDto>? = emptyList(),
    val name: String? = "",
    @param:Json(name = "release_date") val releaseDate: String? = "",
    @param:Json(name = "total_tracks") val totalTracks: Int? = 0,
    val type: String? = "",
    val uri: String? = ""
)