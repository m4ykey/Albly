package com.m4ykey.albly.album.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumItemDto(
    @SerialName("album_type") val albumType: String? = null,
    val artists: List<AlbumArtistDto>? = emptyList(),
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val id: String? = null,
    val images: List<ImageDto>? = emptyList(),
    val name: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("total_tracks") val totalTracks: Int? = null,
    val type: String? = null
)