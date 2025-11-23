package com.m4ykey.albly.album.data.network.model

data class TrackItemDto(
    val artists: List<AlbumArtistDto>? = emptyList(),
    val disc_number: Int? = 0,
    val duration_ms: Int? = 0,
    val explicit: Boolean? = false,
    val external_urls: ExternalUrlsDto? = null,
    val id: String? = "",
    val name: String? = "",
    val preview_url: String? = "",
    val track_number: Int? = 0
)