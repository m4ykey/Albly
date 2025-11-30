package com.m4ykey.albly.album.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackItemDto(
    val artists: List<AlbumArtistDto>? = emptyList(),
    @SerialName("disc_number") val discNumber: Int? = null,
    @SerialName("duration_ms") val durationMs: Int? = null,
    val explicit: Boolean? = false,
    @SerialName("external_urls") val externalUrls: ExternalUrlsDto? = null,
    val id: String? = null,
    val name: String? = null,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("track_number") val trackNumber: Int? = null
)